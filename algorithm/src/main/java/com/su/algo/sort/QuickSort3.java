package com.su.algo.sort;

import com.su.algo.Utils;

/**
 * 快速排序
 */
public class QuickSort3 implements ArrSortInterface {

    public static int[] arr0 = {-2, 2, 5, 4, 5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    @Override
    public int[] sort(int[] arr) {
        sort0(arr, 0, arr.length - 1);
        return arr;
    }

    public static void sort0(int[] arr, int start, int end) {
        if (start < end) {
            int partition = partition(arr, start, end);
            sort0(arr, start, partition - 1);
            sort0(arr, partition + 1, end);
        }
    }

    public static int partition(int[] arr, int start, int end) {
        int x = arr[end];
        int i = start - 1;
        int j = start - 1;
        while (j++ < end) {
            if (arr[j] < x) {
                i++;
                Utils.swap(arr, i, j);
            }
        }
        Utils.swap(arr, i + 1, end);
        return i + 1;
    }

    public static void main(String[] args) {
        QuickSort3 sort3 = new QuickSort3();
        Utils.printArr(arr0);
        sort3.sort(arr0);
        Utils.printArr(arr0);
    }
}
