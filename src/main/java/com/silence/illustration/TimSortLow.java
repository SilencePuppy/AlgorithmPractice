package com.silence.illustration;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * TimSort 简易版实现
 *
 * @author 李晓冰
 * @date 2020年03月08日
 */
public class TimSortLow<T> {

    private static final int MIN_MERGE = 32;

    private final T[] a;
    private final Comparator<? super T> c;

    private T[] tmp;
    private int tmpBase;
    private int tmpLen;

    private int stackSize = 0;
    private final int[] runBase;
    private final int[] runLen;

    public TimSortLow(T[] a, Comparator<? super T> c) {
        this.a = a;
        this.c = c;
        int len = a.length >>> 1;

        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), len);
        tmp = newArray;
        tmpBase = 0;
        tmpLen = len;

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


    private static <T> void binarySort(T[] a, int lo, int start, int hi, Comparator<? super T> c) {
        // 边界处理
        if (start == lo)
            start++;

        for (; start < hi; start++) {
            int left = lo;
            int right = start;
            T pivot = a[start];
            while (left < right) {
                int mid = (right + left) >>> 1;
                if (c.compare(a[mid], pivot) > 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
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
