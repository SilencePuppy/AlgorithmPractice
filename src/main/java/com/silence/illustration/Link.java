package com.silence.illustration;

/**
 * 链表练习
 * 1.反转单链表
 * 2.链表环的检测
 * 3.两个有序链表合并
 * 4.删除链表中第n个节点
 * 5.求链表中间节点
 * @author 李晓冰
 * @date 2020年02月16日
 */
public class Link<T extends Comparable<T>> {
    private Node<T> head;
    private Node<T> tail;

    public Link() {
        head = tail = new Node<>(null, null);
    }

    public void add(T data) {
        tail.next = new Node<>(data, null);
        tail = tail.next;
    }

    public void reverseLink() {
        Node<T> prev = tail.next;
        Node<T> p = head.next, next = null;
        while (p != null) {
            next = p.next;
            p.next = prev;
            prev = p;
            p = next;
        }
        tail = head.next;
        head.next = prev;
    }

    public Link<T> mergeSortedLink(Link<T> otherLink) {
        Node<T> p1 = head.next, p2 = otherLink.head.next;
        Link<T> newLink = new Link<>();
        while (p1 != null && p2 != null) {
            if (p1.data.compareTo(p2.data) <= 0) {
                newLink.add(p1.data);
                p1 = p1.next;
            } else {
                newLink.add(p2.data);
                p2 = p2.next;
            }
        }
        Node<T> p = p1 == null ? p2 : p1;
        while (p != null) {
            newLink.add(p.data);
            p = p.next;
        }
        return newLink;
    }

    public T findMiddle() {
        Node<T> p1 = head.next, p2 = head.next;
        int step = 0;
        while (p2 != null) {
            step = 0;
            if (p2.next != null) {
                step++;
                p2 = p2.next;
                if (p2.next != null) {
                    step++;
                    p2 = p2.next;
                }
            }
            if (step != 2) {
                break;
            } else {
                p1 = p1.next;
            }
        }
        return p1.data;
    }

    public T deleteNode(int index) {
        Node<T> prev = head, p = head;
        int i = -1;
        while (i != index && p != null) {
            i++;
            prev = p;
            p = p.next;
        }
        if (p == null) {
            return null;
        }

        prev.next = p.next;
        p.next = null;
        return p.data;
    }


    public void pirntLink() {
        Node<T> p = head.next;
        while (p != null) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    public static class Node<T extends Comparable<T>> {
        T data;
        Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Link<Integer> link = new Link<>();
        link.add(1);
        link.add(2);
        link.add(3);
//        Link<Integer> link2 = new Link<>();
//        link2.add(3);
//        link2.add(3);
//        link2.add(5);
//        Link<Integer> link1 = link.mergeSortedLink(link2);
//        link1.pirntLink();
//        link1.deleteNode(1);
//        link1.pirntLink();

        System.out.println(link.findMiddle());
    }
}
