package com.silence.fiveminutes;

import com.silence.fiveminutes.tree.avl.rb.RBTree;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
public class TreeTest {

    public static void main(String[] args) {
        RBTree<Integer> bst2 = new RBTree<>();
        bst2.insert(50);
        bst2.insert(30);
        bst2.insert(70);
        bst2.insert(60);
        bst2.insert(80);
        bst2.insert(90);
        bst2.delete(30);
        bst2.printTree(bst2.getRoot());
        System.out.println();
    }
}
