package com.silence.fiveminutes;

import com.silence.fiveminutes.tree.NodeUtil;
import com.silence.fiveminutes.tree.avl.AvlTree;

import java.util.Random;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
public class TreeTest {

    public static void main(String[] args) {
        AvlTree<Integer> avlTree = new AvlTree<>();
        Random random = new Random(System.currentTimeMillis());
        int i=30;
        while (i-- > 0) {
            avlTree.insert(random.nextInt(100));
        }
        int treeLayer = NodeUtil.getTreeLayer(avlTree.getRoot());
        NodeUtil.printBeautifulTree(avlTree.getRoot(), treeLayer);
    }
}
