package com.su.algo;

/**
 * 堆排序
 * todo 建堆和调整堆的过程可以用递归和非递归方式再次实现一边
 */
public class MaxHeapSort implements ArrSortInterface {

    public static int[] arr0 = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    @Override
    public int[] sort(int[] arr) {
        buildMaxHeap(arr);
        for (int i = arr.length - 1; i > 0; i--) {
            Utils.swap(arr, 0, i);
            adjust(arr, 0, i);
        }
        return arr;
    }

    public static void main(String[] args) {
        Utils.printArr(arr0);
        MaxHeapSort.buildMaxHeap(arr0);
        for (int i = arr0.length - 1; i > 0; i--) {
            Utils.swap(arr0, 0, i);
            adjust(arr0, 0, i);
        }
        Utils.printArr(arr0);
    }

    public static void buildMaxHeap(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int current = i;
            int parentIndex = (current - 1) / 2;
            while (arr[current] > arr[parentIndex] && current > 0) {
                Utils.swap(arr, current, parentIndex);
                current = parentIndex;
                parentIndex = (current - 1) / 2;
            }
        }
    }

    public static void adjust(int[] arr, int parentIndex, int heapSize) {
        int parentValue = arr[parentIndex];
        int leftIndex = parentIndex * 2 + 1;
        int rightIndex = parentIndex * 2 + 2;
        if (hasLeftChild(parentIndex, heapSize) && hasRightChild(parentIndex, heapSize)) {
            int leftValue = arr[leftIndex];
            int rightValue = arr[rightIndex];
            int maxChildValue = leftValue > rightValue ? leftValue : rightValue;
            int maxChildIndex = leftValue > rightValue ? leftIndex : rightIndex;
            if (maxChildValue > parentValue) {
                Utils.swap(arr, parentIndex, maxChildIndex);
                adjust(arr, maxChildIndex, heapSize);
                return;
            }
        } else if (hasLeftChild(parentIndex, heapSize)) {
            if (arr[leftIndex] > parentValue) {
                Utils.swap(arr, parentIndex, leftIndex);
            }

        } else if (hasRightChild(parentIndex, heapSize)) {
            if (arr[rightIndex] > parentValue) {
                Utils.swap(arr, parentIndex, rightIndex);
            }
        }
    }

    public static boolean hasLeftChild(int parentIndex, int heapSize) {
        return (parentIndex * 2 + 1) <= (heapSize - 1);
    }

    public static boolean hasRightChild(int parentIndex, int heapSize) {
        return (parentIndex * 2 + 2) <= (heapSize - 1);
    }
}
