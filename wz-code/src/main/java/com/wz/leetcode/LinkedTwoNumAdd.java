package com.wz.leetcode;

/**
 * 两数相加
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 示例：
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class LinkedTwoNumAdd {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode root = null;
        ListNode next = null;
        ListNode temp = null;

        int adder = 0;
        int num = 0;
        int i = 1;
        do {
            int sum = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + adder;
            if (sum < 10) {
                adder = 0;
                num = sum;
            } else {
                adder = sum / 10;
                num = sum % 10;
            }

            if (i == 1) {
                root = new ListNode(num);
                next = root;
            } else {
                temp = next;
                next = new ListNode(num);
                temp.next = next;
            }
            i++;
            l1 = l1 != null ? l1.next : null;
            l2 = l2 != null ? l2.next : null;
        } while (l1 != null || l2 != null);

        if (adder > 0) {
            ListNode higher = new ListNode(adder);
            next.next = higher;
        }
        return root;
    }


    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
