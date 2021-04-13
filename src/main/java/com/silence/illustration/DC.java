package com.silence.illustration;

/**
 * @author 李晓冰
 * @date 2020年02月08日
 */
public class DC {

    public int count(Object[] arr, int index) {
        if (index >= arr.length) {
            return 0;
        } else {
            return 1 + count(arr, index + 1);
        }
    }

    public int sum(Integer[] arr, int index) {
        if (index >= arr.length) {
            return 0;
        } else {
            return arr[index] + sum(arr, index + 1);
        }
    }

    public int max(Integer[] arr, int index) {
        if (index == arr.length - 1) {
            return arr[index];
        } else {
            int otherMax = max(arr, index + 1);
            return otherMax > arr[index] ? otherMax : arr[index];
        }
    }

    public int binarySearch(Integer[] array, int left, int right, int target) {
        if (left > right)
            return -1;

        int middle = right - (right - left) / 2;
        if (array[middle] == target) {
            return middle;
        } else if (array[middle] > target) {
            return binarySearch(array, left, middle - 1, target);
        } else {
            return binarySearch(array, middle + 1, right, target);
        }
    }


    public static void main(String[] args) {
        DC dc = new DC();
        Integer[] arr = new Integer[]{2, 3, 3, 4, 4, 5};
        System.out.println(dc.count(arr, 0));
        System.out.println(dc.sum(arr, 0));
        System.out.println(dc.max(arr, 0));
        System.out.println(dc.binarySearch(arr, 0, arr.length - 1, 4));
    }
}
