package com.silence.fiveminutes.tree;

import com.silence.illustration.Stack;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年03月29日
 */
public class NodeUtil {
    private static final Integer MAX_NODE_DATA = 100;

    /**
     * 随机创建一棵二叉树，通过 @param{maxLayer} 控制数的层数
     *
     * @param maxLayer 目标树的层数
     * @return 树根节点
     */
    public static Node<Integer> createTree(int maxLayer) {
        if (maxLayer <= 0) {
            return null;
        }
        Random random = new Random(System.currentTimeMillis());
        @SuppressWarnings("unnecessaryLocalVariable")
        Node<Integer> root = createChildTree(1, maxLayer, random);
        return root;
    }

    /**
     * 创建子树，根据生成的随机数判断当前子树左右子节点是否需要存在，
     * 然后在递归生成左子树或者右子树，最后创建自己的节点数据并返回。
     * 递归出口，当前节点所在的层数超过了可用的最大层数。
     *
     * @param curLayer 当前子树根节点所在的层数
     * @param maxLayer 树的最大层数
     * @param random   树节点数据随机生成器
     * @return 子树根节点
     */
    private static Node<Integer> createChildTree(int curLayer, int maxLayer, Random random) {
        // 超过了最大层数
        if (curLayer > maxLayer) {
            return null;
        }
        Node<Integer> left = null, right = null;
        // 根据随机数判断是否需要左子树和右子树
        final int r = random.nextInt(3);
        if (r == 0) {
            left = createChildTree(curLayer + 1, maxLayer, random);
        } else if (r == 1) {
            right = createChildTree(curLayer + 1, maxLayer, random);
        } else {
            left = createChildTree(curLayer + 1, maxLayer, random);
            right = createChildTree(curLayer + 1, maxLayer, random);
        }
        Integer curData = random.nextInt(MAX_NODE_DATA);
        @SuppressWarnings("unnecessaryLocalVariable")
        Node<Integer> curNode = new Node<>(curData, left, right);
        return curNode;
    }

    /**
     * 打印树结构，假设第n层每个节点需要w位占位，则任何相邻的两个兄弟节点其中间就会有2w-2(取的数值都是两位的)个空位
     * 如果想让第n-1层第节点在下面一层的两个点中间，则其必须从w-2处开始（2w-2-2）/2,所以每次n-1层的占位都是第n层的2倍
     *
     * @param root     根节点
     * @param maxLayer 树层数，根据该值计算每层需要使用的空格数
     */
    public static void printBeautifulTree(Node<Integer> root, int maxLayer) {
        @Data
        @AllArgsConstructor
        class BeautifulNode {
            Node<Integer> node;
            int curLayer;
        }
        final int baseDataWidth = 2;
        // 封装当前节点和其所在层数从根往下从1开始
        BeautifulNode curNode = new BeautifulNode(root, 1);
        Queue<BeautifulNode> queue = new LinkedList<>();
        queue.add(curNode);

        int curLayer = 0;
        int blank = 0;
        while (true) {
            curNode = queue.remove();
            if (curNode.getCurLayer() > maxLayer) {
                break;
            }
            // 不是同一层就打印换行符
            if (curNode.getCurLayer() != curLayer) {
                curLayer = curNode.getCurLayer();
                // 每一层都是其下面一层占位数量的2倍
                blank = (int) (Math.pow(2, maxLayer - curLayer) * baseDataWidth);
                if (curLayer != 1) {
                    System.out.println();
                }
            }
            if (curNode.getNode() == null) {
                System.out.printf("%" + blank + "d", -1);
                System.out.printf("%" + blank + "s", "");
                queue.add(new BeautifulNode(null, curLayer + 1));
                queue.add(new BeautifulNode(null, curLayer + 1));
            } else {
                System.out.printf("%" + blank + "d", curNode.getNode().getData());
                // 保证任意两个节点之间的距离都是2w-2,     %5d会右对齐，左边部空格
                System.out.printf("%" + blank + "s", "");
                queue.add(new BeautifulNode(curNode.getNode().getLeft(), curLayer + 1));
                queue.add(new BeautifulNode(curNode.getNode().getRight(), curLayer + 1));
            }
        }
        System.out.println();
    }

    /**
     * 计算某一个节点所在的层数
     *
     * @param node 节点
     * @return 层数 null 为0层
     */
    public static int getNodeLayer(Node<Integer> node) {
        if (node == null) {
            return 0;
        }
        int leftHieght = 0, rightHeight = 0;
        leftHieght = getNodeLayer(node.getLeft());
        rightHeight = getNodeLayer(node.getRight());
        return Math.max(leftHieght, rightHeight) + 1;
    }

    /**
     * 通过迭代计算树的层数
     *
     * @param root 树根
     * @return 树的层数
     */
    public static int getTreeLayer(Node<Integer> root) {
        if (root == null) {
            return 0;
        }
        int layer = 0;
        int layerNodeNum = 1;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            if (node.getLeft() != null) {
                queue.add(node.getLeft());
            }
            if (node.getRight() != null) {
                queue.add(node.getRight());
            }
            layerNodeNum--;
            if (layerNodeNum == 0) {
                layer++;
                layerNodeNum = queue.size();
            }
        }
        return layer;
    }

    /**
     * 包含状态的树节点
     * 状态用来通过栈模拟递归时使用
     */
    @Data
    @AllArgsConstructor
    private static class StateNode {
        private Node<Integer> node;
        private int state;
    }

    /**
     * 先序遍历
     *
     * @param node
     * @return
     */
    public static void beforeRecursive(Node<Integer> node) {
        if (node == null) {
            return;
        }
        print(node.getData());
        beforeRecursive(node.getLeft());
        beforeRecursive(node.getRight());
    }

    /**
     * 通过记录节点访问状态的形式来进行先序遍历
     *
     * @param root
     */
    public static void beforeIterator1(Node<Integer> root) {
        Stack<StateNode> stack = new Stack<>();
        StateNode node = new StateNode(root, 0);
        stack.push(node);
        while (stack.size() != 0) {
            node = stack.get();
            if (node.getState() == 0) {
                print(node.getNode().getData());
                node.setState(1);
                if (node.getNode().getLeft() != null) {
                    node = new StateNode(node.getNode().getLeft(), 0);
                    stack.push(node);
                }
            } else if (node.getState() == 1) {
                node.setState(2);
                if (node.getNode().getRight() != null) {
                    node = new StateNode(node.getNode().getRight(), 0);
                    stack.push(node);
                }
            } else {
                stack.pop();
            }
        }
        System.out.println();
    }

    /**
     * 通过栈直接模拟函数递归
     *
     * @param root
     */
    public static void beforeIterator2(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        Node<Integer> node = root;
        while (node != null || stack.size() != 0) {
            if (node != null) {
                print(node.getData());
                stack.push(node.getRight());
                node = node.getLeft();
            } else {
                node = stack.pop();
            }
        }
        System.out.println();
    }

    /**
     * 中序遍历递归
     *
     * @param node
     */
    public static void middleRecursive(Node<Integer> node) {
        if (node == null) {
            return;
        }
        middleRecursive(node.getLeft());
        print(node.getData());
        middleRecursive(node.getRight());
    }

    /**
     * 状态记录迭代中序遍历
     */
    public static void middleIterator1(Node<Integer> root) {
        Stack<StateNode> stack = new Stack<>();
        StateNode node = new StateNode(root, 0);
        stack.push(node);
        while (stack.size() != 0) {
            node = stack.get();
            if (node.getState() == 0) {
                node.setState(1);
                if (node.getNode().getLeft() != null) {
                    stack.push(new StateNode(node.getNode().getLeft(), 0));
                }
            } else if (node.getState() == 1) {
                print(node.getNode().getData());
                node.setState(2);
                if (node.getNode().getRight() != null) {
                    stack.push(new StateNode(node.getNode().getRight(), 0));
                }
            } else {
                stack.pop();
            }
        }
        System.out.println();
    }

    /**
     * 栈迭代 中序遍历
     *
     * @param root
     */
    public static void middleIterator2(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        Node<Integer> node = root;
        while (node != null || stack.size() != 0) {
            if (node != null) {
                stack.push(node);
                node = node.getLeft();
            } else {
                node = stack.pop();
                print(node.getData());
                node = node.getRight();
            }
        }
        System.out.println();
    }

    /**
     * 后序遍历递归
     *
     * @param node
     */
    public static void afterRecursive(Node<Integer> node) {
        if (node == null) {
            return;
        }
        afterRecursive(node.getLeft());
        afterRecursive(node.getRight());
        print(node.getData());
    }

    /**
     * 状态迭代 后序
     *
     * @param root
     */
    public static void afterIterator1(Node<Integer> root) {
        Stack<StateNode> stack = new Stack<>();
        StateNode node = new StateNode(root, 0);
        stack.push(node);
        while (stack.size() != 0) {
            node = stack.get();
            if (node.getState() == 0) {
                node.setState(1);
                if (node.getNode().getLeft() != null) {
                    node = new StateNode(node.getNode().getLeft(), 0);
                    stack.push(node);
                }
            } else if (node.getState() == 1) {
                node.setState(2);
                if (node.getNode().getRight() != null) {
                    node = new StateNode(node.getNode().getRight(), 0);
                    stack.push(node);
                }
            } else {
                print(node.getNode().getData());
                stack.pop();
            }
        }
        System.out.println();
    }

    /**
     * 双栈记录顺序法
     *
     * @param root
     */
    public static void afterIterator2(Node<Integer> root) {
        Stack<Node<Integer>> stack1 = new Stack<>();
        Stack<Node<Integer>> stack2 = new Stack<>();
        Node<Integer> node = root;
        stack1.push(node);
        while (stack1.size() != 0) {
            node = stack1.pop();
            stack2.push(node);
            if (node.getLeft() != null) {
                stack1.push(node.getLeft());
            }
            if (node.getRight() != null) {
                stack1.push(node.getRight());
            }
        }
        while (stack2.size() != 0) {
            print(stack2.pop().getData());
        }
        System.out.println();
    }

    public static void afterIterator3(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        Node<Integer> prev = null, cur = root;
        while (cur != null || stack.size() != 0) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.getLeft();
            } else {
                // 左边遍历完了
                // 返回某一个树的根节点
                cur = stack.get();
                // 有右子树，而且上一个打印的节点不是其右子节点，所以要去先访问右
                if (cur.getRight() != null && !cur.getRight().equals(prev)) {
                    cur = cur.getRight();
                } else {
                    // 没有右或者右已经全部遍历完则直接打印自己 curNode不可能为null
                    cur = stack.pop();
                    print(cur.getData());
                    prev = cur;
                    // 控制弹栈，相当于当前函数递归结束，返回上一层
                    cur = null;
                }
            }
        }
        System.out.println();
    }

    public static void print(int data) {
        System.out.printf("%d ", data);
    }
}
