package com.silence.fiveminutes.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 李晓冰
 * @date 2020年03月15日
 */
@Getter
@Setter
public class Node<T extends Comparable<T>> {
    private T data;
    private Node<T> left;
    private Node<T> right;
    private Node<T> parent;
    private int height;//节点的高度

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public Node(T data, Node<T> parent, int height) {
        this.data = data;
        this.parent = parent;
        this.height = height;
    }

    public Node(T data, Node<T> left, Node<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append("Node{ data=" + data );
        sb.append(", height=" + height);
        sb.append(", parent=");
        if (parent == null) {
            sb.append("null");
        } else {
            sb.append(parent.getData());
        }
        sb.append( ", left=");
        if (left == null) {
            sb.append("null");
        } else {
            sb.append(left.toString());
        }
        sb.append(", right=");
        if (right == null) {
            sb.append("null");
        } else {
            sb.append(right.toString());
        }
        sb.append("}");

        return sb.toString();
    }
}
