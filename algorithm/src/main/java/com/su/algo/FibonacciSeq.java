package com.su.algo;

/**
 * 斐波那契数列
 */
public class FibonacciSeq {

    //递归算法
    public static int getFibonacciRecursion(int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 1;
        }
        return getFibonacciRecursion(n - 1) + getFibonacciRecursion(n - 2);
    }

    //循环算法
    public static int getFibonacci(int index) {
        if (index == 0) {
            return 1;
        }
        if (index == 1) {
            return 1;
        }
        int[] memorandum = new int[index + 1];
        memorandum[0] = 1;
        memorandum[1] = 1;
        for (int i = 2; i <= index; i++) {
            memorandum[i] = memorandum[i - 1] + memorandum[i - 2];

        }
        return memorandum[index];
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            int n = i;
//            int seq = getFibonacciRecursion(n);
//            System.out.println("Recursion n=" + n + " ,seq=" + seq);
//            int seq1 = getFibonacci(n);
//            System.out.println("seq n=" + n + " ,seq=" + seq1);
//        }

        Integer i = 19990;
        int j = 19990;
        System.out.println(i==j);

    }
}
