package com.silence.fiveminutes;

import com.silence.fiveminutes.tree.Node;
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
        int deep = 5;
        SearchTree tree = SearchTree.createTree(deep, System.currentTimeMillis());
        Node.printTreeBeautiful(tree.getRoot(), deep);
        tree.midOrderIterator();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        Integer target = scanner.nextInt();
        tree.delete(target);
        Node.printTreeBeautiful(tree.getRoot(),deep);
    }
}
