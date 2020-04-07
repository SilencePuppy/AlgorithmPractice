package com.silence.fiveminutes.tree.avl;

import com.silence.fiveminutes.tree.Node;

/**
 * AVL树
 *
 * @author 李晓冰
 * @date 2020年04月01日
 */
public class AvlTree<T extends Comparable<T>> {
    private Node<T> root;

    public Node<T> getRoot() {
        return root;
    }

    /**
     * The balance factor limit value
     */
    private static final int LIMIT_BALANCE_FACOTR = 2;

    public void insert(T data) {
        Node<T> node = root;
        Node<T> target = null;
        // only to prevent root is null
        while (node != null) {
            if (node.getData().compareTo(data) <= 0) {
                if (node.getRight() == null) {
                    target = new Node<>(data, node, 0);
                    node.setRight(target);
                    break;
                } else {
                    node = node.getRight();
                }
            } else {
                if (node.getLeft() == null) {
                    target = new Node<>(data, node, 0);
                    node.setLeft(target);
                    break;
                } else {
                    node = node.getLeft();
                }
            }
        }

        if (target == null) {
            target = new Node<>(data, null, 0);
            root = target;
        }
        // 重新计算所有祖先节点高度
        resetParentHeight(target,null);

        Node<T> minUnBalancedTree = getMinUnBalancedTree(target);
        if (minUnBalancedTree != null) {
            adjustUnbalancedTree(minUnBalancedTree);
        }
    }

    /**
     * 获取最小不平衡子树
     *
     * @param node 开始查询节点
     * @return 最小不平衡子树
     */
    private Node<T> getMinUnBalancedTree(Node<T> node) {
        while (node != null) {
            if (Math.abs(getBalanceFactor(node)) == LIMIT_BALANCE_FACOTR) {
                break;
            } else {
                node = node.getParent();
            }
        }
        resetParentHeight(node, null);
        return node;
    }

    /**
     * 调整最小不平衡子树
     *
     * @param node 最小不平衡子树根节点
     */
    private void adjustUnbalancedTree(Node<T> node) {
        if (getBalanceFactor(node) == LIMIT_BALANCE_FACOTR) {
            leftChildHigher(node);
        } else {
            rightChildHigher(node);
        }
    }

    /**
     * recalculate the height of the nodes from node and it's parents until ceilNode
     *
     * @param node     begin node
     * @param ceilNode ceil limit(exclusive)
     */
    private void resetParentHeight(Node<T> node, Node<T> ceilNode) {
        if (node == null) {
            return;
        }
        while (node != ceilNode) {
            node.setHeight(1 + Math.max(getNodeHeight(node.getLeft()), getNodeHeight(node.getRight())));
            node = node.getParent();
        }
    }

    /**
     * 获得平衡因子
     *
     * @param node 目标节点
     * @return if node is null then return 0
     * otherwise return left child height - right child height
     */
    public int getBalanceFactor(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return getNodeHeight(node.getLeft()) - getNodeHeight(node.getRight());
    }

    /**
     * 获取节点高度
     *
     * @param node 目标节点
     * @return if node is null then return 0 otherwise node.getHeight();
     */
    public int getNodeHeight(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.getHeight();
    }

    /**
     * node is not balanced and it's left child is higher than right child
     *
     * @param node the minimum unBalanced tree
     */
    private void leftChildHigher(Node<T> node) {
        Node<T> leftChild = node.getLeft();
        // need left rotate first
        if (getBalanceFactor(leftChild) < 0) {
            rotateLeft(leftChild);
            resetParentHeight(leftChild, node);
        }

        rotateRight(node);
        resetParentHeight(node, null);
    }

    /**
     * node is not balanced and it's right child is higher than left child
     *
     * @param node the minimum unBalanced tree
     */
    private void rightChildHigher(Node<T> node) {
        Node<T> rightChild = node.getRight();
        // need right rotate first
        if (getBalanceFactor(rightChild) > 0) {
            rotateRight(rightChild);
            resetParentHeight(rightChild, node);
        }

        rotateLeft(node);
        resetParentHeight(node, null);
    }


    private void rotateLeft(Node<T> node) {
        Node<T> rightChild = node.getRight();

        node.setRight(rightChild.getLeft());
        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(node);
        }

        rightChild.setParent(node.getParent());
        if (node.getParent() == null) {
            root = rightChild;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(rightChild);
        } else {
            node.getParent().setRight(rightChild);
        }

        rightChild.setLeft(node);
        node.setParent(rightChild);
    }

    private void rotateRight(Node<T> node) {
        Node<T> leftChild = node.getLeft();

        node.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
        }

        leftChild.setParent(node.getParent());
        if (node.getParent() == null) {
            root = leftChild;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(leftChild);
        } else {
            node.getParent().setRight(leftChild);
        }

        leftChild.setRight(node);
        node.setParent(leftChild);
    }
}
