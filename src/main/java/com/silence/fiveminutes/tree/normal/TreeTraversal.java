package com.silence.fiveminutes.tree.normal;

import com.silence.fiveminutes.tree.Node;

import java.util.LinkedList;

/**
 * 二叉树遍历代码
 * @author 李晓冰
 * @date 2020年03月15日
 */
public class TreeTraversal {

    /**
     * 先序 递归
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void preOrderRecursive(Node<T> node) {
        if (node == null) {
            return;
        }
        print(node.getData());
        preOrderRecursive(node.getLeft());
        preOrderRecursive(node.getRight());
    }

    /**
     * 先序迭代
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void preOrderIterator(Node<T> node) {
        if (node == null) {
            return;
        }
        LinkedList<Node<T>> stack = new LinkedList<>();
        Node<T> curNode = node;
        // 如果进入了一个空的左或右节点则curNode可能就是null,但是这时候如果栈里面还有数据则表示
        // 当前数还没有遍历完。还有一种可能是某一根的左侧遍历完了，要去遍历非空右节点，这时栈里面就会暂时没数据
        while (curNode != null || stack.size() != 0) {
            if (curNode != null) {
                // 当前子树的根节点
                print(curNode.getData());
                // 先把右节点入栈然后进入做节点，模拟递归中的左侧递归
                stack.push(curNode.getRight());
                curNode = curNode.getLeft();
            } else {
                // 某一个节点的左或右节点是null，这时候需要弹栈（也就是函数栈里面从当前函数返回上一个函数，处理右节点）
                curNode = stack.poll();
            }
        }
    }

    /**
     * 中序 递归
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void midOrderRecursive(Node<T> node) {
        if (node == null) {
            return;
        }
        midOrderIterator(node.getLeft());
        print(node.getData());
        midOrderIterator(node.getRight());
    }

    public <T extends Comparable<T>> void midOrderIterator(Node<T> node) {
        LinkedList<Node<T>> stack = new LinkedList<>();
        Node<T> curNode = node;

        while (curNode != null || stack.size() != 0) {
            if (curNode != null) {
                // 入栈暂存，模拟递归左子树
                stack.push(curNode);
                curNode = curNode.getLeft();
            } else {
                // 模拟 左子树已经是空了，这时候就要回到上一层函数并打印中间节点

                // 这里不用判断null，因为我压入的都是非null的
                curNode = stack.poll();
                print(curNode.getData());
                // 递归右子树，如果curNode还是null没有右子树，则表示某一个子树的左子树已经全部遍历完了，这时候
                // 就需要让它弹栈，打印子树的根节点，并探测其右子树
                curNode = curNode.getRight();
            }
        }
    }

    /**
     * 后序 递归
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void afterOrderRecursive(Node<T> node) {
        if (node == null) {
            return;
        }
        afterOrderRecursive(node.getLeft());
        afterOrderRecursive(node.getRight());
        print(node.getData());
    }

    /**
     * 后序遍历相对来说比较复杂，因为前序弹栈的时候我们可以确定弹出来的一定是某一个树的右节点，可以直接打印
     * 而中序弹出来的肯定是某一子树的根节点，也是可以直接打印，然后再去探测右子树。
     * 但是后序我们需要判断当前弹出来的数据（是否已经遍历过了左右子树，相当于前两个递归都处理返回后才能去处理中间的）
     */

    /**
     * 辅助栈方法，相当于按照先序遍历顺序将节点反过来打印一遍
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void afterOrderIterator(Node<T> node) {
        LinkedList<Node<T>> stack = new LinkedList<>();
        // 本栈用来存储已经完成左右子树遍历的节点
        LinkedList<Node<T>> stack2 = new LinkedList<>();
        // 准备工作
        stack.push(node);
        Node<T> curNode;
        // 模拟先序遍历
        while (stack.size() != 0) {
            curNode = stack.poll();
            stack2.push(curNode);
            if (curNode.getLeft() != null) {
                stack.push(curNode.getLeft());
            }
            if (curNode.getRight() != null) {
                stack.push(curNode.getRight());
            }
        }
        while (stack2.size() != 0) {
            print(stack2.poll().getData());
        }
    }

    /**
     * 计数方法，记住每一个节点被访问的次数，当次数达到3时就可以直接输出了
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void afterOrderIterator2(Node<T> node) {
        class NodeWrap {
            Node<T> node;
            int count;

            public NodeWrap(Node<T> node) {
                this.node = node;
            }
        }
        LinkedList<NodeWrap> stack = new LinkedList<>();
        NodeWrap curNode = new NodeWrap(node);
        while (true) {
            if (curNode.node != null) {
                curNode.count++;
                // 第一次访问则入栈并进入其左子树
                if (curNode.count == 1) {
                    stack.push(curNode);
                    curNode = new NodeWrap(curNode.node.getLeft());
                } else if (curNode.count == 2) {
                    // 第2次访问则入栈并进入其右子树
                    stack.push(curNode);
                    curNode = new NodeWrap(curNode.node.getRight());
                } else {
                    // 第三次访问直接输出，而且表明以curNode为根节点的子树已经遍历完了，所以通过弹栈访问
                    // curNode的兄弟节点，相当于递归中的返回上一层函数
                    print(curNode.node.getData());
                    // 出口
                    if (stack.size() == 0) {
                        break;
                    }
                    curNode = stack.poll();
                }
            } else {
                // 左子树或者右子树是null
                curNode = stack.poll();
            }
        }
    }

    /**
     * prev标记法，如果想打印中间节点，那么上一次打印的节点一定是当前中间节点的右子节点
     * 或者其没有右子节点，否则就应该先去其左右子节点
     * @param node
     * @param <T>
     */
    public <T extends Comparable<T>> void afterOrderIterator3(Node<T> node) {
        LinkedList<Node<T>> stack = new LinkedList<>();
        Node<T> curNode = node, prev = null;
        while (curNode != null || stack.size() != 0) {
            if (curNode != null) {
                // 模拟递归左子树
                stack.push(curNode);
                curNode = curNode.getLeft();
            } else {
                // 左边遍历完了
                // 返回某一个树的根节点
                curNode = stack.getFirst();
                // 有右子树，而且上一个打印的节点不是其右子节点，所以要去先访问右
                if (curNode.getRight() != null && !curNode.getRight().equals(prev)) {
                    curNode = curNode.getRight();
                } else {
                    // 没有右或者右已经全部遍历完则直接打印自己 curNode不可能为null
                    curNode = stack.poll();
                    print(curNode.getData());
                    prev = curNode;
                    // 控制弹栈，相当于当前函数递归结束，返回上一层
                    curNode = null;
                }
            }
        }
    }

    private void print(Object o) {
        System.out.print(o.toString() + " ");
    }
}
