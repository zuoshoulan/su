package com.su.algo;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/7/29 01:11
 */
public class PopSort implements SortInterface {


    public static void main(String[] args) {
        PopSort popSort = new PopSort();
        int[] arr = {10, 14, 11, 9, 15, 20, 3, 5, 2, 1, 0, 34, 43, 4};
        popSort.sort(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(arr.length);
    }

    @Override
    public void sort(int[] arr) {
        for (int j = 0; j < arr.length - 1; j++) {
            for (int i = 0; i < arr.length - j - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    int k = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = k;
                }
            }
        }
    }
}
