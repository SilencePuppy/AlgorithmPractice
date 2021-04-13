package com.silence.fiveminutes.tree.btree;

import java.util.LinkedList;

/**
 * B树
 * @author 李晓冰
 * @date 2020年05月19日
 */
public class BTree<K extends Comparable<K>, V> {

    private BNode<K, V> root;

    public BTree() {
        this.root = new BNode<>(true);
    }

    public V find(K k) {
        BNode<K, V> node = get(k);
        if (node == null) {
            return null;
        } else {
            int i = node.binarySearch(k);
            return node.getKVNodeByIndex(i).getV();
        }
    }

    private BNode<K, V> get(K k) {
        BNode<K, V> node = root;
        while (node != null) {
            int index = node.binarySearch(k);
            if (index < node.getNKey() && node.getKVNodeByIndex(index).getK().compareTo(k) == 0) {
                return node;
            }
            if (node.isLeaf()) {
                node = null;
            } else {
                node = node.getChildByIndex(index);
            }
        }
        return null;
    }

    public void insert(K k, V v) {
        KVNode<K, V> kvNode = new KVNode<>(k, v);
        BNode<K, V> node = root;
        int index;
        while (true) {
            index = node.binarySearch(k);
            if (index < node.getNKey() && node.getKVNodeByIndex(index).getK().compareTo(k) == 0) {
                return;
            }
            if (node.isLeaf()) {
                break;
            } else {
                node = node.getChildByIndex(index);
            }
        }
        node.insertKVNode(index, kvNode);
        fixInsert(node);
    }

    private void fixInsert(BNode<K, V> node) {
        while (node.isFull()) {
            node = node.split();
        }
        if (node.getParent() == null) {
            root = node;
        }
    }

    public V remove(K k) {
        BNode<K, V> node = get(k);
        if (node == null) {
            return null;
        }
        int index = node.binarySearch(k);
        V retV = node.getKVNodeByIndex(index).getV();

        // 待删除节点转移为后继节点
        if (!node.isLeaf()) {
            BNode<K, V> successor = node.getSuccessor(node, index + 1);
            node.setKVNodeByIndex(index, successor.getKVNodeByIndex(0));
            node = successor;
            index = 0;
        }
        node.removeKVNode(index);

        fixDelete(node);

        return retV;
    }

    private void fixDelete(BNode<K, V> node) {
        if (node == root) {
            return;
        }
        while (node.isLess()) {
            int inParentIndex = node.findInParentIndex();
            BNode<K, V> preSibling = node.getPreSibling(inParentIndex);
            if (preSibling != null && preSibling.isOverMin()) {
                node.borrowFromPreSibling(inParentIndex, preSibling);
                break;
            }

            BNode<K, V> succeSibling = node.getSucceSibling(inParentIndex);
            if (succeSibling != null && succeSibling.isOverMin()) {
                node.borrowFromSucceSibling(inParentIndex, succeSibling);
                break;
            }

            if (node.getParent().getLastChild() == node) {
                BNode<K, V> pre = node.getPreSibling(inParentIndex);
                pre.mergeWithSuccesSibling(inParentIndex - 1, node);
                node = pre;
            } else {
                BNode<K, V> succe = node.getSucceSibling(inParentIndex);
                node.mergeWithSuccesSibling(inParentIndex, succe);
            }
            if (node.getParent() == null) {
                break;
            } else {
                node = node.getParent();
            }
        }
        if (node.getParent() == null) {
            root = node;
        }
    }

    public void print() {
        int lineNodeNum = 1;
        int nextLineNodeNum = 0;
        LinkedList<BNode<K, V>> list = new LinkedList<>();
        list.add(root);
        while (list.size() != 0) {
            if (lineNodeNum == 0) {
                System.out.println();
                lineNodeNum = nextLineNodeNum;
                nextLineNodeNum = 0;
            }
            BNode<K, V> node = list.removeFirst();
            System.out.print(node + " ");
            lineNodeNum--;

            list.addAll(node.getAllChildren());
            nextLineNodeNum += node.getAllChildren().size();
        }
        System.out.println();
    }
}
