package com.silence.fiveminutes.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年03月15日
 */
public class TreeCreator {

    /**
     * 每个节点的最大数值
     */
    private static final int MAX_NODE_DATA = 100;

    /**
     * 控制生成多少层节点
     */
    public static final int MAX_LAYER = 5;

    /**
     * 随机创建一棵二叉树，通过MAX_LAYER控制数的层数
     * @return 树根节点
     */
    public static Node<Integer> createTree() {
        final Random random = new Random(System.currentTimeMillis());
        @SuppressWarnings("UnnecessaryLocalVariable")
        Node<Integer> root = createChildTree(0, MAX_LAYER, random);
        return root;
    }

    /**
     * 创建子树，根据生成的随机数判断当前子树左右子节点是否需要存在，
     * 然后在递归生成左子树或者右子树，最后创建自己的节点数据并返回。
     * 递归出口，当前节点所在的层数超过了可用的最大层数。
     * @param layerNum 当前子树根节点所在的层数
     * @param maxLayer 树的最大层数
     * @param random   树节点数据随机生成器
     * @return 子树根节点
     */
    private static Node<Integer> createChildTree(int layerNum, int maxLayer, Random random) {
        // 超过了最大层数
        if (layerNum >= maxLayer) {
            return null;
        }
        // 根据随机数判断是否需要左子树和右子树
        Node<Integer> left = null, right = null;
        int i = random.nextInt(3);
        // 需要左子节点
        if (i == 0) {
            left = createChildTree(layerNum + 1, maxLayer, random);
        } else if (i == 1) {
            // 需要右子节点
            right = createChildTree(layerNum + 1, maxLayer, random);
        } else {
            // 左右都需要
            left = createChildTree(layerNum + 1, maxLayer, random);
            right = createChildTree(layerNum + 1, maxLayer, random);
        }
        // 当前节点数值
        int data = random.nextInt(MAX_NODE_DATA);
        @SuppressWarnings("UnnecessaryLocalVariable")
        Node<Integer> node = new Node<>(data, left, right);
        return node;
    }

    /**
     * 打印树结构，假设第n层每个节点需要w位占位，则任何相邻的两个兄弟节点其中间就会有2w-2(取的数值都是两位的)个空位
     * 如果想让第n-1层第节点在下面一层的两个点中间，则其必须从w-2处开始（2w-2-2）/2,所以每次n-1层的占位都是第n层的2倍
     * @param root 根节点
     * @param maxLayer 树层数，根据该值计算每层需要使用的空格数
     */
    public static void printTreeBeautiful(Node<Integer> root, Integer maxLayer) {
        // 最下一层每个节点数值占的位数
        final int baseBlankNum = 2;
        // 封装当前节点和其所在层数从根往下从1开始
        BeautifulNode n = new BeautifulNode(root, 1);
        LinkedList<BeautifulNode> queue = new LinkedList<>();
        queue.add(n);

        int oldLayer = 0;
        int blank = 0;
        while (true) {
            BeautifulNode node = queue.poll();
            // 到达最下面节点+1位置
            if (node.getLayer() > maxLayer) {
                break;
            }
            // 不是同一层就打印换行符
            if (oldLayer != node.getLayer()) {
                oldLayer = node.getLayer();
                // 每一层都是其下面一层占位数量的2倍
                blank = (int) (Math.pow(2,maxLayer - oldLayer) * baseBlankNum);
                System.out.println();
            }
            if (node.getNode() == null) {
                // 空节点也要打印
                System.out.printf("%" + blank + "d", -1);
                System.out.printf("%" + blank + "s", "");
                queue.add(new BeautifulNode(null, node.getLayer() + 1));
                queue.add(new BeautifulNode(null, node.getLayer() + 1));
            } else {
                System.out.printf("%" + blank + "d", node.getNode().getData());
                // 保证任意两个节点之间的距离都是2w-2,     %5d会右对齐，左边部空格
                System.out.printf("%" + blank + "s", "");
                queue.add(new BeautifulNode(node.getNode().getLeft(), node.getLayer() + 1));
                queue.add(new BeautifulNode(node.getNode().getRight(), node.getLayer() + 1));
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class BeautifulNode {
        Node<Integer> node;
        int layer;
    }
}
