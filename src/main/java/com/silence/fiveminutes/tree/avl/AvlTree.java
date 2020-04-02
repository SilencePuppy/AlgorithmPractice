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
     * 破坏平衡的平衡因子数量
     */
    private static final int LIMIT_LEFT_FACTOR = 2;
    private static final int LIMIT_RIGHT_FACTOR = -2;

    public void insert(T data) {
        Node<T> p = root;
        Node<T> newNode = null;
        while (p != null) {
            if (data.compareTo(p.getData()) >= 0) {
                if (p.getRight() == null) {
                    newNode = new Node<>(data, p, 1);
                    p.setRight(newNode);
                    break;
                } else {
                    p = p.getRight();
                }
            } else {
                if (p.getLeft() == null) {
                    newNode = new Node<>(data, p, 1);
                    p.setLeft(newNode);
                    break;
                } else {
                    p = p.getLeft();
                }
            }
        }

        if (p == null) {
            newNode = new Node<>(data, null, 1);
            root = newNode;
        }

        // 获取最小不平衡子树
        Node<T> unbalancedNode = newNode.getParent();
        while (unbalancedNode != null) {
            int height = Math.max(getHeight(unbalancedNode.getLeft()), getHeight(unbalancedNode.getRight()));
            unbalancedNode.setHeight(height + 1);
            int bf = getBalanceFactor(unbalancedNode);
            if (Math.abs(bf) == LIMIT_LEFT_FACTOR) {
                break;
            }
            unbalancedNode = unbalancedNode.getParent();
        }
        if (unbalancedNode != null) {
            balanceNode(unbalancedNode);
        } else {
            resetParentLinkHeight(newNode, null);
        }

    }

    /**
     * 重新计算从node到ceilNode路径上节点的高度
     *
     * @param node     重新计算从node到ceilNode路径上节点的高度不包含ceilNode
     * @param ceilNode 上限节点
     */
    private void resetParentLinkHeight(Node<T> node, Node<T> ceilNode) {
        Node<T> p = node;
        while (p != ceilNode) {
            int height = Math.max(getHeight(p.getLeft()), getHeight(p.getRight()));
            p.setHeight(height + 1);
            p = p.getParent();
        }
    }

    /**
     * 获取node节点的高度
     *
     * @param node 当前节点
     * @return 高度 null 高度为0
     */
    private int getHeight(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.getHeight();
    }

    /**
     * 通过左子树高度减去右子树高度计算平衡因子
     *
     * @param node target node
     * @return 平衡因子
     */
    private int getBalanceFactor(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.getLeft()) - getHeight(node.getRight());
    }

    /**
     * 使一个不平衡最小子树变成平衡树
     *
     * @param node 不平衡子树节点
     */
    private void balanceNode(Node<T> node) {
        int balanceFactor = getBalanceFactor(node);

        // 左子树高
        if (balanceFactor == LIMIT_LEFT_FACTOR) {
            leftChildHigher(node);
        } else {
            // 右子树高
            rightChildHigher(node);
        }
    }

    /**
     * 左子树超过高度，最终需要右旋
     */
    private void leftChildHigher(Node<T> node) {
        Node<T> leftChild = node.getLeft();
        int df = getBalanceFactor(leftChild);
        // 需要先进行左旋
        if (df < 0) {
            rotateLeft(leftChild);
            resetParentLinkHeight(leftChild, node);
        }
        // 进行右旋
        rotateRight(node);
        resetParentLinkHeight(node, null);
    }

    /**
     * 右子树超过高度,最终需要左旋
     */
    private void rightChildHigher(Node<T> node) {
        Node<T> rightChild = node.getRight();
        int bf = getBalanceFactor(rightChild);
        if (bf > 0) {
            rotateRight(rightChild);
            resetParentLinkHeight(rightChild, node);
        }
        // 进行左旋
        rotateLeft(node);
        resetParentLinkHeight(node, null);
    }

    /**
     * 左旋
     *
     * @param node 待处理节点
     */
    private void rotateLeft(Node<T> node) {
        Node<T> rightChild = node.getRight();

        // node右子树的左孩子设置为node的右子树
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

    /**
     * 右旋
     *
     * @param node 待处理节点
     */
    private void rotateRight(Node<T> node) {
        Node<T> leftChild = node.getLeft();

        // node的左孩子的右子树设置到node的左子树上(如果没有右子树就是将node节点左孩子置null)
        node.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
        }

        // 跟新node父节点的子节点的指向
        leftChild.setParent(node.getParent());
        if (node.getParent() == null) {
            root = leftChild;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(leftChild);
        } else {
            node.getParent().setRight(leftChild);
        }

        // node节点设置为左孩子的右子树上
        leftChild.setRight(node);
        node.setParent(leftChild);
    }
}
