package com.su.algo.datastructure;

import lombok.Data;

@Data
public class ListNode<T> {


    private T data;

    private ListNode next;

    ListNode(T data) {
        data = data;
        next = null;
    }

    public ListNode(T[] list, int offset) {
        data = list[offset];
        if (offset < list.length - 1) {
            next = new ListNode(list, offset + 1);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {9, 8, 7, 6, 5};
        ListNode head = new ListNode(arr, 0);
        System.out.println();
    }
}
