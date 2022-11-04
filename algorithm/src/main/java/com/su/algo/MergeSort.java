package com.su.algo;

/**
 * 归并排序
 * todo 使用非递归方式实现一遍
 */
public class MergeSort implements ArrSortInterface {

    public static int[] arr0 = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};


    @Override
    public int[] sort(int[] arr) {
        sort0(arr, 0, arr.length - 1);
        return arr;
    }


    public static void sort0(int[] arr, int start, int end) {
        if ((end - start + 1) > 1) {
            int mid = getMid(start, end);
            sort0(arr, start, mid);
            sort0(arr, mid + 1, end);
            merge(arr, start, end);
        }
    }

    public static void merge(int[] arr, int start, int end) {
        int mid = getMid(start, end);
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
        int[] arr1 = {1, 3, 0};
//        Utils.printArr(arr1);
//        MergeSort.merge(arr1, 0, arr1.length - 1);
//        Utils.printArr(arr1);
        System.out.println("---");
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(arr1);
        Utils.printArr(arr1);
    }

    public static int getMid(int start, int end) {
        return (start + end) / 2;
    }

}
