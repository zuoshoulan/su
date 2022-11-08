package com.su.algo.sort;

import com.su.algo.Utils;

/**
 * 归并排序 非递归方式实现
 */
public class MergeSort2 implements ArrSortInterface {

    public static int[] arr0 = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};


    @Override
    public int[] sort(int[] arr) {
        sort0(arr);
        return arr;
    }


    public static void sort0(int[] arr) {
        for (int pageSize = 1; pageSize <= arr.length; pageSize = pageSize * 2) {
            System.out.println("pageSize: " + pageSize);
            int totalPage = Double.valueOf(Math.ceil((double) arr.length / pageSize)).intValue();
            for (int pageNum = 0; pageNum < totalPage; pageNum = pageNum + 2) {
                if (pageNum * pageSize + pageSize - 1 < arr.length - 1) {
                    merge(arr, pageNum * pageSize, pageNum * pageSize + pageSize * 2 - 1, pageNum * pageSize + pageSize - 1);
                }
            }
        }
    }

    public static void merge(int[] arr, int start, int end, int mid) {
        if (end > arr.length) {
            end = arr.length - 1;
        }
        int[] tmp = new int[end - start + 1];
        int leftIndex = start;
        int rightIndex = mid + 1;
        int tmpIndex = 0;

        while (leftIndex <= mid && rightIndex <= end) {
            if (arr[leftIndex] < arr[rightIndex]) {
                tmp[tmpIndex++] = arr[leftIndex++];
            } else {
                tmp[tmpIndex++] = arr[rightIndex++];
            }
        }
        while (leftIndex <= mid) {
            tmp[tmpIndex++] = arr[leftIndex++];
        }
        while (rightIndex <= end) {
            tmp[tmpIndex++] = arr[rightIndex++];
        }

        tmpIndex = 0;
        for (int i = start; i <= end; i++) {
            arr[i] = tmp[tmpIndex++];
        }

    }

    public static void main(String[] args) {
        int[] arr1 = {6, 3, 0};
        Utils.printArr(arr0);
        System.out.println("---");
        MergeSort2 mergeSort = new MergeSort2();
        mergeSort.sort(arr0);
        Utils.printArr(arr0);
        double ceil = Math.ceil((double) arr1.length / 2);
        System.out.println();

    }

}
