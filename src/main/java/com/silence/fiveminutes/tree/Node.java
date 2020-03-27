package com.silence.fiveminutes.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;

/**
 * @author 李晓冰
 * @date 2020年03月15日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Node<T extends Comparable<T>> {
    private T data;
    private Node<T> left;
    private Node<T> right;

    public Node(T data) {
        this.data = data;
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
        System.out.println();
    }

    @Data
    @AllArgsConstructor
    public static class BeautifulNode {
        Node<Integer> node;
        int layer;
    }
}
