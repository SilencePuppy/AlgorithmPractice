package com.silence.illustration;

import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年02月07日
 */
public class Sort {


    /***************练习题***************/

    /**
     * 假如我有一个英文字符数组，里面有大写有小写，现在要求将所有小写字母都排到大写字母之前。
     * 思路1：设置首尾指针，前者遇到大写就停止，后者遇到小写就停止。然后两者进行交换并继续前行，直到相遇。
     * 思路2：基于桶排序进行操作。
     * 变换：如果我里面还有数字和符合，要求先按照小写、大写、数字、符号进行排列，然后要求其内部按照ascii排序。
     * 思路：桶排序，每个桶要求内部有序
     *
     * @param arr
     */
    public void sortCharArr(char[] arr) {
        int i = 0, j = arr.length - 1;
        while (i < j) {
            while ('a' <= arr[i] && arr[i] <= 'z') {
                i++;
            }
            while ('A' <= arr[j] && arr[j] <= 'Z') {
                j--;
            }
            char c = arr[i];
            arr[i] = arr[j];
            arr[j] = c;
            i++;
            j--;
        }
    }

    /**
     * 在n的时间复杂度内，找到第k大的数字。
     * 基于快排思想，先随机抽取一个哨兵，找到该哨兵位置，如果该下标就是k则返回该哨兵，
     * 如果下标小于k就去其右边找，否则去其左边找。
     * 第一次执行n次， 第二次n/2,第三次n/4 等比数列求和为 2n-1
     */
    public int findTarget(int[] arr, int k) {
        // 数据错误
        if (k > arr.length || k < 1) {
            return -1;
        }
        int target = findTarget(0, arr.length - 1, arr, k);
        return target;
    }

    private int findTarget(int left, int right, int[] arr, int k) {
        int place = setRightPlace(left, right, arr);
        // k从1开始
        if (place + 1 == k) {
            return arr[place];
        } else if (place + 1 > k) {
            return findTarget(left, place - 1, arr, k);
        } else {
            return findTarget(place + 1, right, arr, k);
        }
    }

    /***************各种排序算法***************/

    /**
     * 计数排序，基于桶排序的一种。适合小范围数据排序。时间复杂度O(n+k) k是数据的范围
     * 最后根据每一个桶内数字个数进行找位置的时候要从后往前找，否则就不是稳定排序了。
     *
     * @param arr
     */
    public void countSort(int[] arr) {
        if (arr.length <= 1) {
            return;
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        int[] countArr = new int[max + 1];
        for (int i = 0; i < arr.length; i++) {
            countArr[arr[i]]++;
        }
        // countArr[i] 表示数组中小于等于i值的数字的个数。
        for (int i = 1; i < countArr.length; i++) {
            countArr[i] = countArr[i] + countArr[i - 1];
        }
        int[] sortedArr = new int[arr.length];
        // 从后往前，保证稳定排序
        for (int i = arr.length - 1; i >= 0; i--) {
            int index = countArr[arr[i]] - 1;
            sortedArr[index] = arr[i];
            countArr[arr[i]]--;
        }
        for (int i = 0; i < sortedArr.length; i++) {
            arr[i] = sortedArr[i];
        }
    }

    /**
     * 原地排序，非稳定排序，
     *
     * @param arr
     */
    public void selectSort(int[] arr) {
        int minIndex;
        for (int i = 0; i < arr.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(arr, minIndex, i);
            }
        }
    }

    /**
     * 稳定排序
     */
    public void bubbleSort(int[] arr) {
        boolean flag;
        for (int i = 1; i < arr.length; i++) {
            flag = true;
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
        }
    }

    /**
     * 插入排序比冒泡的优势就是，插入每次移动数据和冒泡是交互数据，多了两次操作
     *
     * @param arr
     */
    public void insertSort(int[] arr) {
        int temp;
        int j;
        for (int i = 1; i < arr.length; i++) {
            temp = arr[i];
            for (j = i - 1; j >= 0; j++) {
                if (arr[j] > temp) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = temp;
        }
    }

    /**
     * 原地，非稳定，平均复杂度是n*logn，最坏情况是n^2
     *
     * @param arr
     */
    public void quickSort(int[] arr) {
        quickSort(0, arr.length - 1, arr);
    }

    private void quickSort(int left, int right, int[] arr) {
        if (left >= right) {
            return;
        }
        int place = setRightPlace(left, right, arr);
        quickSort(left, place - 1, arr);
        quickSort(place + 1, right, arr);
    }

    private int setRightPlace(int left, int right, int[] arr) {
        Random random = new Random(System.currentTimeMillis());
        int randomIndex;
        if (left == right) {
            randomIndex = 1;
        } else {
            randomIndex = random.nextInt(right - left) + left;
        }
        swap(arr, right, randomIndex);
        int j = left;
        for (int i = left; i < right; i++) {
            if (arr[i] < arr[right]) {
                if (j != i) {
                    swap(arr, j, i);
                }
                j++;
            }
        }
        swap(arr, j, right);
        return j;
    }

    /**
     * 非原地排序，空间复杂度n,时间复杂度是n*logn
     * T(n) = 2T(n/2)+n
     *
     * @param arr
     */
    public void mergeSort(int[] arr) {
        merge(0, arr.length - 1, arr);
    }

    private void merge(int left, int right, int[] arr) {
        if (left >= right) {
            return;
        }
        int middle = (right - left) / 2 + left;
        merge(left, middle, arr);
        merge(middle + 1, right, arr);
        merge(left, middle, middle + 1, right, arr);
    }

    private void merge(int leftStart, int leftEnd, int rightStart, int rightEnd, int[] arr) {
        int[] temp = new int[rightEnd - leftStart + 1];
        int i = leftStart, j = rightStart, k = 0;

        while (i <= leftEnd && j <= rightEnd) {
            if (arr[i] <= arr[j]) {
                temp[k] = arr[i];
                i++;
            } else {
                temp[k] = arr[j];
                j++;
            }
            k++;
        }

        int start = i, end = leftEnd;
        if (i > leftEnd) {
            start = j;
            end = rightEnd;
        }
        while (start <= end) {
            temp[k] = arr[start];
            start++;
            k++;
        }
        for (i = leftStart, k = 0; i <= rightEnd; i++, k++) {
            arr[i] = temp[k];
        }
    }

    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{22, 11, 32, 12, 12, 32, 55, 3, 213, 21, 22, 11};
        Sort sort = new Sort();
        int target = sort.findTarget(arr, 1);
        System.out.println(target);
        sort.selectSort(arr);
        sort.bubbleSort(arr);
        sort.insertSort(arr);
        sort.mergeSort(arr);
        sort.quickSort(arr);
        sort.countSort(arr);
        for (int data : arr) {
            System.out.print(data);
            System.out.print(" ");
        }
        System.out.println();
        char[] chars = new char[]{'a', 'b', 'c', 'D', 'd', 'Z', 'B'};
        sort.sortCharArr(chars);
        for (char data : chars) {
            System.out.print(data);
            System.out.print(" ");
        }
    }
}
