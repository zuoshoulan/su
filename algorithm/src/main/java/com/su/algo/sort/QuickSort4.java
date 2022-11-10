package com.su.algo.sort;

import com.su.algo.Utils;

/**
 * 快速排序 左右两边互换
 */
public class QuickSort4 implements ArrSortInterface {

    public static int[] arr0 = {-2, 2, 5, 4, 5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    @Override
    public int[] sort(int[] arr) {
        sort0(arr, 0, arr.length - 1);
        return new int[0];
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
        int i = start, j = end - 1;
        while (i < j) {
            while (arr[i] <= x && i < j) {
                i++;
            }
            //arr[i] > x 或者
            while (arr[j] > x && i < j) {
                j--;
            }
            //arr[j] <= x
            if (i < j) {
                Utils.swap(arr, i, j);
                i++;
                j--;
            }

        }


        if (arr[i] > x) {
            Utils.swap(arr, i, end);
            return i;
        } else {
            Utils.swap(arr, i + 1, end);
            return i + 1;
        }
    }

    public static void main(String[] args) {
        QuickSort4 sort4 = new QuickSort4();
        Utils.printArr(arr0);
        sort4.sort(arr0);
        Utils.printArr(arr0);
    }
}
