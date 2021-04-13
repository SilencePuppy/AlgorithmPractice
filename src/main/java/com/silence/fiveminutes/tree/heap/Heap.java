package com.silence.fiveminutes.tree.heap;

/**
 * 堆
 * @author 李晓冰
 * @date 2020年04月20日
 */
public class Heap<T extends Comparable<T>> {
    Object[] array;
    // 总容量
    int capacity;
    // 当前已存放数量
    int count;

    public Heap(int capacity) {
        this.capacity = capacity;
        array = new Object[capacity + 1];
    }

    public void insert(T data) {
        if (count == capacity) {
            return;
        }
        array[++count] = data;
        shiftUp(array, count);
    }

    public T removeTop() {
        if (count == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        T retData = (T) array[1];

        array[1] = array[count--];
        shiftDown(array, count, 1);
        return retData;
    }


    /**
     * 对传入的数组进行堆排序
     * @param array 待处理数组 数据下标从1开始
     * @param count 有效数据长度
     */
    public void sort(Object[] array, int count) {
        buildHeap(array, count);
        for (int i = count; i > 1; i--) {
            swap(array, 1, i);
            shiftDown(array, i - 1, 1);
        }
    }

    private void buildHeap(Object[] array, int count) {
        int n = count / 2;
        for (int i = n; i > 0; i--) {
            shiftDown(array, count, i);
        }
    }


    private void shiftDown(Object[] array, int n, int i) {
        int maxPos = i;
        while (true) {
            int leftChild = i * 2;
            if (leftChild <= n && compare(array[i], array[leftChild]) < 0) maxPos = leftChild;
            if (leftChild + 1 <= n && compare(array[maxPos], array[leftChild + 1]) < 0) maxPos = leftChild + 1;

            if (maxPos == i) break;
            swap(array, i, maxPos);
            i = maxPos;
        }
    }

    private void shiftUp(Object[] array, int i) {
        int parent = i / 2;
        while (parent != 0 && compare(array[i], array[parent]) > 0) {
            swap(array, i, parent);
            i = parent;
            parent = i / 2;
        }
    }

    private int compare(Object o1, Object o2) {
        @SuppressWarnings("unchecked")
        T t1 = (T) o1;
        @SuppressWarnings("unchecked")
        T t2 = (T) o2;
        return t1.compareTo(t2);
    }

    private void swap(Object[] array, int i, int j) {
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    public void printBeautiful() {
        int treeHeight = (int) (Math.ceil(Math.log(count)) + 1);
        final int baseBlank = 2;
        // 当前层每一个节点使用的占位数量
        int layerBlank = 0;
        int i = 1, curLayer = 0;

        while (i <= count) {
            int selfLayer = (int) (Math.floor(Math.log(i) / Math.log(2)) + 1);
            if (curLayer != selfLayer) {
                curLayer = selfLayer;
                layerBlank = (int) (Math.pow(2, treeHeight - curLayer) * baseBlank);
                if (curLayer != 1) {
                    System.out.println();
                }
            }
            System.out.printf(String.format("%%%dd", layerBlank), array[i]);
            System.out.printf(String.format("%%%ds", layerBlank), "");
            i++;
        }
    }
}
