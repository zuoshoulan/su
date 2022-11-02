package com.su.algo;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/8/16 00:35
 */
public class Utils {


    public static void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    public static void printArr(int[] arr) {
        printArr(arr, 0, arr.length);
    }

    public static void printArr(int[] arr, int startInclusive, int endExclusive) {
        StringBuilder sb = new StringBuilder();
        sb.append("arr:");
        sb.append("[");
        for (int i = startInclusive; i < endExclusive; i++) {
            sb.append(arr[i]).append(" ");
        }
        sb.append("]");
        System.out.println(sb);
    }
}
