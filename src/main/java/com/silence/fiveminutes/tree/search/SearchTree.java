package com.silence.fiveminutes.tree.search;

import com.silence.fiveminutes.tree.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
@Data
public class SearchTree {
    Node<Integer> root;

    public void insert(int data) {
        if (root == null) {
            root = new Node<>(data);
            return;
        }

        Node<Integer> p = root;
        while (p != null) {
            if (data >= p.getData()) {
                if (p.getRight() == null) {
                    p.setRight(new Node<>(data, p));
                    return;
                } else {
                    p = p.getRight();
                }
            } else {
                if (p.getLeft() == null) {
                    p.setLeft(new Node<>(data, p));
                    return;
                } else {
                    p = p.getLeft();
                }
            }
        }
    }

    public static SearchTree createTree(int dataNum) {
        SearchTree tree = new SearchTree();
        Random random = new Random(System.currentTimeMillis());
        while (dataNum-- > 0) {
            int i = random.nextInt(100);
            tree.insert(i);
        }
        return tree;
    }

    public List<Node<Integer>> find(int data) {
        List<Node<Integer>> retList = new ArrayList<>();
        if (root == null) {
            return retList;
        }
        Node<Integer> p = root;
        while (p != null) {
            if (data == p.getData()) {
                retList.add(p);
                p = p.getRight();
            } else if (data > p.getData()) {
                p = p.getRight();
            } else {
                p = p.getLeft();
            }
        }
        return retList;
    }

    public void delete(int data) {
        List<Node<Integer>> target = find(data);
        for (int i = target.size() - 1; i >= 0; i--) {
            delete(target.get(i));
        }
    }

    private void delete(Node<Integer> target) {
        Node<Integer> pp = target.getParent();
        Node<Integer> p = target;
        if (p.getLeft() != null && p.getRight() != null) {
            Node<Integer> childPP = p;
            Node<Integer> childP = p.getRight();
            while (childP.getLeft() != null) {
                childPP = childP;
                childP = childP.getLeft();
            }
            p.setData(childP.getData());
            pp = childPP;
            p = childP;
        }
        Node<Integer> child = p.getRight() == null ? p.getLeft() : p.getRight();
        if (p == root) {
            root = child;
            return;
        }
        if (pp.getLeft() == p) {
            pp.setLeft(child);
        } else {
            pp.setRight(child);
        }
        // 释放
        p.setParent(null);
        p.setLeft(null);
        p.setRight(null);
    }

}
