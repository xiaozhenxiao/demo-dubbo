package com.wz.toutiao;

public class CircularLlinkedList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        detectCycle(head);
    }

    public static ListNode detectCycle(ListNode head) {
        if(head == null || head.next == null){
            System.out.println("no cycle");
            return head;
        }
        ListNode slow = head.next;
        ListNode fast = head.next.next;
        while(slow!=null && fast != null && slow.val != fast.val){
            slow = slow.next;
            fast = fast.next==null?null:fast.next.next;
        }
        if(slow==null || fast==null){
            System.out.println("no cycle");
            return head;
        }
        int i = 0;
        while(slow.val != head.val){
            slow = slow.next;
            head = head.next;
            i++;
        }
        System.out.println("tail connects to node index " + i);

        return head;
    }
}
