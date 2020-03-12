package com.silence.illustration;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * TimSort 简易版实现
 * 总体思想，结合了二分插入排序和归并排序，当数量较少时直接使用二分插入排序，否则将数据按照递增情况
 * 划分出一个一个的run(就是从当前索引开始向后遍历数组，看是否是递增或者严格递减如果是递减就反转该部分),
 * 如果划分出的run的长度小于一个指定值(取数字最高5个bit位组成的值，如果是奇数就再加一)，就用二分插入方法
 * 将后面的数据补充到run内直到达到指定的长度，然后将满足条件的run压入栈中。栈中的run的长度必须满足第任意
 * i个run
 * 1） runLen[i-2]>runLen[i-1]+runLen[i]
 * 2） runLen[i-1]>run[i]
 * 个人认为第二个条件保证了整个栈内每个run长是下大上小，条件1则是尽量使从上到下进行归并的时候两个子数组的长度
 * 尽量一致。如果压栈的时候发现数据不满足条件就将其进行归并。
 *
 * 在进行归并的时候也使用了很多技巧，首先是找到B数组第一位在A数组中应该在的位置，然后找到A数组最后一位在B中应该在的
 * 位置，这样只需要归并AB数组中间相邻的一部分数据，然后归并时申请的额外空间是取AB处理后较小的一个的长度。
 *
 * java 作者使用数组时喜欢用lo表示数组开始为hi表示最高索引位置加1,也就是对应的长度。这样做在进行代码书写的时候能够
 * 避免索引移动或者求索引相对位置时进行不必要的加一减一操作
 * @author 李晓冰
 * @date 2020年03月08日
 */
public class TimSortLow<T> {
    // 这个取值java作者说是经验告诉他，这样性能好
    private static final int MIN_MERGE = 32;

    // 待排序数组
    private final T[] a;
    // 比较器
    private final Comparator<? super T> c;
    // 在归并排序时使用的额外的辅助存储空间，每次使用的时候都会进行判断是否足够长。
    private T[] tmp;
    private int tmpBase;
    // 辅助空间长度
    private int tmpLen;

    // 保存run的栈
    private int stackSize = 0;
    private final int[] runBase;
    private final int[] runLen;

    /**
     * 比较器用？super T表示我们可以用T类型父类的属性进行比较
     * 比如我已经有了一个比较水果的比较器（比较水果的重量），你现在要比较苹果，那我既可以用
     * 苹果自己的比较器（比较甜度）也可以使用水果的比较器（应为苹果也有重量）进行比较。
     * 就看具体的业务场景需要我用什么。
     * @param a
     * @param c
     */
    public TimSortLow(T[] a, Comparator<? super T> c) {
        this.a = a;
        this.c = c;
        int len = a.length >>> 1;

        // 开始时用数组一般长作为辅助内存长度
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), len);
        tmp = newArray;
        tmpBase = 0;
        tmpLen = len;
        // 这个数值是直接去取的java版本里面可选的最大值，据说49可以满足的数组长度是Integer.MAX_VALUE - 4;
        // 不纠结这个问题，肯定够用
        int stackLen = 49;
        runBase = new int[stackLen];
        runLen = new int[stackLen];
    }

    public static <T> void sort(T[] a, int lo, int hi, Comparator<? super T> c) {
        int nRemaining = hi - lo;
        if (nRemaining < 2) {
            return;
        }
        if (nRemaining < MIN_MERGE) {
            int len = countRunAndMakeAscending(a, lo, hi, c);
            binarySort(a, lo, lo + len, hi, c);
            return;
        }

        TimSortLow<T> tsl = new TimSortLow<>(a, c);
        int minRun = minRunLength(nRemaining);
        do {
            // Identify next run
            int runLen = countRunAndMakeAscending(a, lo, hi, c);
            if (runLen < minRun) {
                int force = nRemaining > minRun ? minRun : nRemaining;
                binarySort(a, lo, lo + runLen, lo + force, c);
                runLen = force;
            }
            tsl.pushRun(lo, runLen);
            tsl.mergeCollapse();

            lo += runLen;
            nRemaining -= runLen;

        } while (nRemaining > 0);

        tsl.mergeForceCollapse();
    }

    /**
     * 二分插入排序，认为start前面的数都是已经排好顺序的，所以从start开始进行排序，先用二分查找找到
     * start位置的元素在a[lo]-a[start-1]里面的正确位置。为了保证是稳定排序，二分找的是小于等于a[start]的
     * 最后一个数字的后面一个位置（也就是第一个大于a[start]的元素位置或者是数组的hi位置）
     * @param a 数组
     * @param lo 要排序的第一个数字的下标
     * @param start 第一个未排序的数字的下标
     * @param hi 数组要排序最后一个数字下标的下一位
     * @param c 比较器
     */
    private static <T> void binarySort(T[] a, int lo, int start, int hi, Comparator<? super T> c) {
        // 边界处理，默认第一个数字是已经排好序的数字
        if (start == lo)
            start++;

        for (; start < hi; start++) {
            int left = lo;
            int right = start;
            T pivot = a[start];
            //二分查找pivot所在的位置，由于right是start,而我们需要进行对比的数字范围是lo-start-1
            //所以left,right只有在结束的时候才会出现指向同一个位置，而如果right指向的是start-1
            //则结束条件就是left<=right(考虑下只有一个元素的情况)
            while (left < right) {
                int mid = (right + left) >>> 1;
                if (c.compare(a[mid], pivot) > 0) {
                    // 如果目标值大于pivot则移动right,注意：right表示的是待查找自范围的末尾元素位置加1
                    right = mid;
                } else {
                    // 如果目标值小于或等于pivot则left为mid的下一位，因为我们要找的是最后一个小于等于pivot的下一位
                    left = mid + 1;
                }
            }
            // 表示有多少个元素需要移动位置，这里如果start表示的是最后一个元素的位置，而不是最后一个元素的下一位，
            // 那么n =start-left+1,加一是为了包含自身，而我们的start是最后一个元素的下一位，所以就不用减一，
            // 少了一个计算
            int n = start - left;
            switch (n) {
                case 2:
                    a[left + 2] = a[left + 1];
                case 1:
                    a[left + 1] = a[left];
                    break;
                default:
                    System.arraycopy(a, left, a, left + 1, n);
            }
            a[left] = pivot;
        }


    }

    private static <T> int countRunAndMakeAscending(T[] a, int lo, int hi, Comparator<? super T> c) {
        int runHi = lo + 1;
        //just one element
        if (runHi == hi) {
            return 1;
        }

        if (c.compare(a[lo], a[runHi++]) > 0) {
            while (runHi < hi && c.compare(a[runHi - 1], a[runHi]) > 0)
                runHi++;
            reverseRange(a, lo, runHi);
        } else {
            while (runHi < hi && c.compare(a[runHi - 1], a[runHi]) <= 0)
                runHi++;
        }
        return runHi - lo;
    }

    private static <T> void reverseRange(T[] a, int lo, int hi) {
        hi--;
        while (lo < hi) {
            T tmp = a[hi];
            a[hi--] = a[lo];
            a[lo++] = tmp;
        }
    }

    private static int minRunLength(int n) {
        int r = 0;
        //MIN_MERGE是32 0010 0000 该方法可以计算n的最高5位的数值是多少
        while (n >= MIN_MERGE) {
            r |= (n & 1);// 计算是否为奇数
            n >>= 1;
        }
        // 奇数就再加1,不知到为什么
        return n + r;
    }

    private void pushRun(int base, int len) {
        runBase[stackSize] = base;
        runLen[stackSize] = len;
        stackSize++;
    }

    private void mergeForceCollapse(){
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (n > 0 && runLen[n - 1] < runLen[n + 1])
                n--;
            mergeAt(n);
        }
    }

    private void mergeCollapse() {
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1]) {
                if (runLen[n - 1] < runLen[n + 1])
                    n--;
                mergeAt(n);
            } else if (runLen[n] <= runLen[n + 1]) {
                mergeAt(n);
            } else {
                break;
            }
        }
    }

    private void mergeAt(int i) {
        assert stackSize > 1;
        assert i >= 0;
        assert i == stackSize - 2 || i == stackSize - 3;

        int base1 = runBase[i];
        int len1 = runLen[i];
        int base2 = runBase[i + 1];
        int len2 = runLen[i + 1];

        assert base1 + len1 == base2;

        runLen[i] = len1 + len2;
        if (i == stackSize - 3) {
            runBase[i + 1] = runBase[i + 2];
            runLen[i + 1] = runLen[i + 2];
        }
        stackSize--;

        int k = gallopRight(a[base2], a, base1, len1, 0, c);
        assert k >= 0;
        base1 += k;
        len1 -= k;
        if (len1 == 0)
            return;

        len2 = gallopLeft(a[base1 + len1 - 1], a, base2, len2, 0, c);
        if (len2 == 0)
            return;

        if (len1 < len2) {
            mergeLo(base1, len1, base2, len2);
        } else {
            mergeHi(base1, len1, base2, len2);
        }

    }

    private void mergeLo(int base1, int len1, int base2, int len2) {
        assert len1 < len2;
        T[] a = this.a;
        T[] tmp = ensureCapacity(len1);
        int cursor1 = tmpBase;
        int cursor2 = base2;
        int dest = base1;
        System.arraycopy(a, base1, tmp, cursor1, len1);

        a[dest++] = a[cursor2++];
        if (--len2 == 0) {
            System.arraycopy(tmp, cursor1, a, dest, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(a, cursor2, a, dest, len2);
            a[dest + len2] = tmp[cursor1];
            return;
        }

        while (len1 > 0 && len2 > 0) {
            if (c.compare(tmp[cursor1], a[cursor2]) <= 0) {
                a[dest++] = tmp[cursor1++];
                len1--;
            } else {
                a[dest++] = a[cursor2++];
                len2--;
            }
        }
        if (len1 == 0) {
            System.arraycopy(a, cursor2, a, dest, len2);
        } else {
            System.arraycopy(tmp, cursor1, a, dest, len1);
        }
    }

    private void mergeHi(int base1, int len1, int base2, int len2) {
        T[] a = this.a;
        T[] tmp = ensureCapacity(len2);
        int tmpBase = this.tmpBase;
        System.arraycopy(a, base2, tmp, tmpBase, len2);

        int cursor1 = base1 + len1 - 1;
        int cursor2 = tmpBase + len2 - 1;
        int dest = base2 + len2 - 1;

        a[dest--] = a[cursor1--];
        if (--len1 == 0) {
            System.arraycopy(tmp, tmpBase, a, dest - len2 + 1, len2);
            return;
        }
        if (len2 == 1) {
            dest = dest - len1;
            cursor1 = cursor1 - len1;
            System.arraycopy(a, cursor1 + 1, a, dest + 1, len1);
            a[dest] = tmp[cursor2];
            return;
        }

        while (len1 > 0 && len2 > 0) {
            if (c.compare(tmp[cursor2], a[cursor1]) >= 0) {
                a[dest--] = tmp[cursor2--];
                len2--;
            } else {
                a[dest--] = a[cursor1--];
                len1--;
            }
        }

        if (len1 == 0) {
            System.arraycopy(tmp, cursor2 - len2 + 1, a, dest - len2 + 1, len2);
        }
    }

    private T[] ensureCapacity(int minCapacity) {
        if (tmpLen < minCapacity) {
            int newSize = minCapacity;
            newSize |= newSize >> 1;
            newSize |= newSize >> 2;
            newSize |= newSize >> 4;
            newSize |= newSize >> 8;
            newSize |= newSize >> 16;
            newSize++;

            if (newSize < 0) {
                newSize = minCapacity;
            } else {
                newSize = Math.min(newSize, a.length >>> 1);
            }
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), newSize);
            tmp = newArray;
            tmpLen = newSize;
            tmpBase = 0;
        }
        return tmp;
    }

    /**
     * Locates the position at which to insert the specified key into the
     * specified sorted range; if the range contains an element equal to key,
     * returns the index of the leftmost equal element.
     *
     * @param key
     * @param a
     * @param base
     * @param len
     * @param hint the index at which to begin the search, 0 <= hint < n.
     *             The closer hint is to the result, the faster this method will run.
     * @param c
     * @return
     */
    public int gallopRight(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
        int left, right;
        if (c.compare(a[base+hint], key) > 0) {
            right = base+hint;
            left = base;
        } else {
            left = base+hint + 1;
            right = base + len;
        }

        while (left < right) {
            int mid = (left + right) >>> 1;
            if (c.compare(a[mid], key) > 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        assert left == right;
        return left - base;
    }

    public int gallopLeft(T key, T[] a, int base, int len, int hint, Comparator<? super T> c) {
        int left, right;
        if (c.compare(a[base+hint], key) < 0) {
            left = base+hint;
            right = base + len;
        } else {
            right = base+hint;
            left = base;
        }

        while (left < right) {
            int mid = (left + right) >>> 1;
            if (c.compare(a[mid], key) >= 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right - base;
    }
}
