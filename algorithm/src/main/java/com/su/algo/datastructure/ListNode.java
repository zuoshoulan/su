package com.su.algo.datastructure;

import lombok.Data;

@Data
public class ListNode<T> {


    private T data;

    private ListNode next;
    private ListNode pre;

    ListNode() {
    }

    ListNode(T data) {
        this.data = data;
        this.next = null;
        this.pre = null;
    }

    public ListNode(T[] list, int offset) {
        data = list[offset];
        if (offset < list.length - 1) {
            next = new ListNode(list, offset + 1);
            next.pre = this;
        }
    }

    public ListNode(T[] list) {
        if (list == null || list.length == 0) {
            return;
        }
        this.data = list[0];
        ListNode tmp = this;
        ListNode node = this;
        for (int i = 1; i < list.length; i++) {
            node = new ListNode(list[i]);
            node.pre = tmp;
            tmp.next = node;
            tmp = tmp.next;
        }
        node.next = this;
        this.pre = node;


    }


    public static void main(String[] args) {
        Integer[] arr = {9, 8, 7, 6, 5};
        Integer[] arr1 = {9};
        ListNode head = new ListNode(arr1, 0);
        ListNode tail = null;
        ListNode tmp = head;
        while (tmp.next != null) {
            tmp = tmp.next;
        }
        tail = tmp;
        tail.next = head;
        head.pre = tail;

        ListNode head1 = new ListNode(arr1);


        System.out.println();
    }
}
