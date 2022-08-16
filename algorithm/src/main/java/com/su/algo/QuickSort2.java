package com.su.algo;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/7/29 01:19
 */
public class QuickSort2 {

    public static void main(String[] args) {
        int[] arr = {10, 14, 11, 9, 15, 20, 3, 5, 10, 2, 1, 0, 34, 10, 43, 4};
        sort(arr, 0, arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    private static void sort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivot = arr[start];
        int left = start;
        int right = end;
        while (left != right) {
            while (left != right && arr[right] > pivot) {
                right--;
            }
            while (left != right && arr[left] <= pivot) {
                left++;
            }
            if (left < right) {
                Utils.swap(arr, left, right);
            }
            System.out.print("");
        }
        int mid = left;
        Utils.swap(arr, start, left);
        sort(arr, start, mid - 1);
        sort(arr, mid + 1, end);
    }


}
