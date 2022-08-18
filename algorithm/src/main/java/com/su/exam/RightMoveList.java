package com.su.exam;

/**
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/8/18 16:23
 */
public class RightMoveList {

    public static void main(String[] args) {
        test2();
    }

    private static void test2() {
        int n = 10;
        int k = 12;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        int real = k % n;
        System.out.println("real:" + real);
        revert(arr, 0, arr.length - 1);
        revert(arr, 0, real - 1);
        revert(arr, real, arr.length - 1);
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    private static void revert(int[] arr, int start, int end) {
        int size = (end - start + 1) / 2;
        for (int i = 0; i < size; i++) {
            swap(arr, start + i, end - i);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    private static void test1() {
        int n = 10;
        int k = 12;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }

        int real = k % n;
        System.out.println("real:" + real);
        int[] temp = new int[real];
        for (int i = 0; i < real; i++) {
            temp[i] = arr[n - real + i];
        }
        for (int i = 0; i < n - real; i++) {
            int p = n - 1 - i;
            arr[p] = arr[p - real];
        }
        for (int i = 0; i < real; i++) {
            arr[i] = temp[i];
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }

    }
}


//原数组
//0 1 2 3 4 5 6 7 8 9
//反转
//9 8 7 6 5 4 3 2 1 0
//8 9 0 1 2 3 4 5 6 7

// 012345

//start + i >= end - i
// 2i>=end-start
//i>=(end-start)/2