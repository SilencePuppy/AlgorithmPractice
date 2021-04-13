package com.silence.fiveminutes.recursive;

import java.util.Stack;

/**
 * 汉诺塔问题，递归和迭代实现方法，
 * 递归可以转化为迭代，但是迭代不一定可以转化为递归，
 * 递归使用方法栈来记录上一次状态，如果迭代使用栈进行的迭代实现，则一般是可以实现的。
 * @author 李晓冰
 * @date 2020年02月24日
 */
public class Hanoid {
    public void hanoidRecursive(int n, char from, char to, char swap) {
        if (n == 1) {
            System.out.println(from + "->" + to);
            return;
        }
        hanoidRecursive(n - 1, from, swap, to);
        hanoidRecursive(1, from, to, swap);
        hanoidRecursive(n - 1, swap, to, from);
    }

    class Node {
        int n;
        char from;
        char to;
        char swap;

        public Node(int n, char from, char to, char swap) {
            this.n = n;
            this.from = from;
            this.to = to;
            this.swap = swap;
        }
    }

    public void hanoidIterate(int n, char from, char to, char swap) {
        if (n == 1) {
            System.out.println(from + "->" + to);
            return;
        } else {

        }

        Stack<Node> stack = new Stack<>();
        stack.push(new Node(n, from, to, swap));
        while (stack.size() != 0) {
            Node node = stack.pop();
            if (node.n == 1) {
                System.out.println(node.from + "->" + node.to);
            } else {
                stack.push(new Node(node.n - 1, node.swap, node.to, node.from));
                stack.push(new Node(1, node.from, node.to, node.swap));
                stack.push(new Node(node.n - 1, node.from, node.swap, node.to));
            }
        }
    }


    public static void main(String[] args) {
        Hanoid hanoid = new Hanoid();
//        hanoid.hanoidRecursive(3,'a','b','c');
        System.out.println("-----------");
        hanoid.hanoidIterate(3, 'a', 'b', 'c');
    }

}
