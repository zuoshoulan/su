package com.su.algo;

/**
 * 插入排序，左边是有序序列，右边是无序序列，把无序序列中的元素一次放入有序序列并使其最终有序的排序方式
 *
 * @author zhengweikang@hz-cpp.com
 * @Date 2022/8/3 16:48
 */
public class InsertArrSort implements ArrSortInterface {

    public static int[] arr = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    public static void sort1(int[] arr0) {
        for (int i = 1; i < arr0.length; i++) {

            //插入排序
            int value = arr0[i];
            int j = i - 1;
            while (j >= 0 && arr0[j] > value) {
                arr0[j + 1] = arr0[j];
                Utils.printArr(arr0);
                j--;
            }
            arr0[j + 1] = value;
        }
        System.out.println("---");
        Utils.printArr(arr0);
    }

    public static void sort2(int[] arr0) {
        for (int i = 1; i < arr0.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr0[j] < arr0[j - 1]) {
                    Utils.swap(arr0, j, j - 1);
                    Utils.printArr(arr0);
                }
            }
        }
    }

    public static void sort3(int[] arr0) {
        for (int i = 1; i < arr0.length; i++) {
            int j = i;
            while (j > 0 && arr0[j] < arr0[j - 1]) {
                Utils.swap(arr0, j, j - 1);
                j--;
                Utils.printArr(arr0);
            }
        }
    }


    public static void main(String[] args) {
        Utils.printArr(arr);
        System.out.println("---");
        sort1(arr);
        System.out.println("---");
        Utils.printArr(arr);
    }

    @Override
    public void sort(int[] arr) {
        sort1(arr);
    }
}
