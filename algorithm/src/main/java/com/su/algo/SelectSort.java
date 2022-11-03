package com.su.algo;

public class SelectSort implements ArrSortInterface {

    public static int[] arr0 = {5, 4, 3, 1, 2, 9, 8, 6, 7, 0};

    public static void main(String[] args) {
        Utils.printArr(arr0);
        System.out.println("---");
        SelectSort sortor = new SelectSort();
        sortor.sort(arr0);
    }

    @Override
    public int[] sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int index = i;
            for (int j = i; j < arr.length; j++) {
                if (arr[j] > arr[index]) {
                    index = j;
                }
            }
            Utils.swap(arr, i, index);
            Utils.printArr(arr);
        }


        return arr;
    }
}
