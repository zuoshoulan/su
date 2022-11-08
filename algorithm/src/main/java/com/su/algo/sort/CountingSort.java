package com.su.algo.sort;

import com.su.algo.Utils;

/**
 * 计数排序
 */
public class CountingSort implements ArrSortInterface {

    public static int[] arr0 = {-2, 2, 5, 4, 5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    @Override
    public int[] sort(int[] arr) {
        int min = getMin(arr);
        int max = getMax(arr);
        int offset = 0;
        if (min < 0) {
            offset = 0 - min;
        }
        int[] tmp = new int[max + offset + 1];

        for (int i = 0; i < arr.length; i++) {
            int value = arr[i];
            tmp[value + offset] = tmp[value + offset] + 1;
        }
        int p = 0;
        for (int i = min + offset; i <= max + offset; i++) {
            int count = tmp[i];
            while (count > 0) {
                arr[p++] = i - offset;
                count--;
            }
        }
        return arr;
    }

    public static int getMin(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }


    public static void main(String[] args) {
        Utils.printArr(arr0);
        CountingSort sort = new CountingSort();
        sort.sort(arr0);
        Utils.printArr(arr0);
    }
}
