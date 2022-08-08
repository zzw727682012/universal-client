package com.code;

public class Code2 {

    /**
     * 给定两个单链表的头节点headA 和 headB ，请找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 null 。
     *
     * 图示两个链表在节点 c1 开始相交：
     *
     *
     *
     * 题目数据 保证 整个链式结构中不存在环。
     *
     * 注意，函数返回结果后，链表必须 保持其原始结构 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/3u1WK4
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param headA
     * @param headB
     * @return
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode firstA = headA;
        while (headA != headB) {
            if (headA.next != null) {
                headA = headA.next;
            } else {
                headB = headB.next;
                if (headB == null) {
                    headA = null;
                } else {
                    headA = firstA;
                }
            }
        }
        return headA;
    }


    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }
}
