package com.silence.fiveminutes;

import com.silence.fiveminutes.tree.btree.BTree;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
public class TreeTest {

    public static void main(String[] args) {
        BTree<Integer,Integer> bTree = new BTree<>();
        for (int i = 1; i <= 6; i++) {
            bTree.insert(i,i);
        }
        bTree.print();
        System.out.println("------");
        bTree.remove(5);
        bTree.print();
        System.out.println("------");
        bTree.remove(4);
        bTree.print();
    }

}
