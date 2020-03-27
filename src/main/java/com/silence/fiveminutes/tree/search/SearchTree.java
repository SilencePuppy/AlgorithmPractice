package com.silence.fiveminutes.tree.search;

import com.silence.fiveminutes.tree.Node;
import com.silence.illustration.Stack;
import lombok.Data;

import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
@Data
public class SearchTree {
    Node<Integer> root;

    public Node<Integer> find(Integer data) {
        Node<Integer> p = root;
        while (p != null) {
            if (data > p.getData()) {
                p = p.getRight();
            } else if (data < p.getData()) {
                p = p.getLeft();
            } else {
                return p;
            }
        }
        return null;
    }

    public void insert(Integer data) {
        if (root == null) {
            root = new Node<>(data);
            return;
        }

        Node<Integer> p = root;
        while (p != null) {
            if (data > p.getData()) {
                if (p.getRight() == null) {
                    p.setRight(new Node<>(data));
                    return;
                }
                p = p.getRight();
            } else if (data < p.getData()) {
                if (p.getLeft() == null) {
                    p.setLeft(new Node<>(data));
                    return;
                }
                p = p.getLeft();
            } else {
                return;
            }
        }
    }

    public void delete(Integer data) {
        Node<Integer> pp = null, p = root;
        while (p != null) {
            if (p.getData().equals(data)) {
                break;
            }
            pp = p;
            if (data < p.getData()) {
                p = p.getLeft();
            } else {
                p = p.getRight();
            }
        }
        if (p == null) {
            return;
        }
        if (p.getRight() != null && p.getLeft() != null) {
            Node<Integer> childPP = p;
            Node<Integer> childp = p.getRight();
            while (childp.getLeft() != null) {
                childPP = childp;
                childp = childp.getLeft();
            }
            p.setData(childp.getData());
            pp = childPP;
            p = childp;
        }

        // 删除的是根节点
        if (pp == null) {
            root =null;
            return;
        }

        Node<Integer> target = p.getRight() == null ? p.getLeft() : p.getRight();
        if (pp.getLeft() == p) {
            pp.setLeft(target);
        } else {
            pp.setRight(target);
        }
    }

    public void midOrderIterator(){
        if (root == null) {
            return;
        }
        Stack<Node<Integer>> stack = new Stack<>();
        Node<Integer> p = root;
        while (p != null || stack.size() > 0) {
            if (p != null) {
                stack.push(p);
                p = p.getLeft();
            } else {
                p = stack.pop();
                System.out.print(p.getData()+" ");
                p = p.getRight();
            }
        }
    }

    public static SearchTree createTree(Integer dataNum, long seed) {
        Random random = new Random(seed);
        int i = 0;
        SearchTree tree = new SearchTree();
        while (i++ < dataNum) {
            int d = random.nextInt(100);
            tree.insert(d);
        }
        return tree;
    }
}
