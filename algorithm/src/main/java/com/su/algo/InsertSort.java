package com.su.algo;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/8/3 16:48
 */
public class InsertSort {

    public static int[] arr = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    public static void main(String[] args) {
        Utils.printArr(arr);
        System.out.println("---");
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    Utils.swap(arr, j, j - 1);
                    Utils.printArr(arr);
                }
            }
        }
        System.out.println("---");
        Utils.printArr(arr);
    }
}
