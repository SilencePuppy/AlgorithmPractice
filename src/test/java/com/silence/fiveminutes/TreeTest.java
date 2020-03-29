package com.silence.fiveminutes;

import com.silence.fiveminutes.tree.NodeUtil;
import com.silence.fiveminutes.tree.search.SearchTree;
import org.junit.Test;

import java.util.Scanner;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
public class TreeTest {

    @Test
    public void SearchTreeText() {

    }

    public static void main(String[] args) {
        SearchTree tree = SearchTree.createTree(10);
        int treeLayer = NodeUtil.getTreeLayer(tree.getRoot());
        NodeUtil.printBeautifulTree(tree.getRoot(), treeLayer);

        Scanner scanner = new Scanner(System.in);
        int targetData = scanner.nextInt();
        tree.delete(targetData);
        NodeUtil.printBeautifulTree(tree.getRoot(),treeLayer);
    }
}
