package com.silence.fiveminutes.tree.normal;

import com.silence.fiveminutes.tree.Node;

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


}
