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
}
