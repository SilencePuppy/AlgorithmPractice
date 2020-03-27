package com.silence.illustration;

import com.silence.fiveminutes.tree.Node;
import com.silence.fiveminutes.tree.normal.TreeCreator;
import com.silence.fiveminutes.tree.normal.TreeTraversal;
import org.junit.Test;

/**
 * @author 李晓冰
 * @date 2020年03月15日
 */
public class TreeTest {
    @Test
    public void testTraversal() {
        Node<Integer> tree = TreeCreator.createTree();
        TreeTraversal treeITraversal = new TreeTraversal();
        treeITraversal.afterOrderRecursive(tree);
        System.out.println();
        treeITraversal.afterOrderIterator2(tree);
        System.out.println();
        treeITraversal.afterOrderIterator3(tree);
        System.out.println();
        TreeCreator.printTreeBeautiful(tree,TreeCreator.MAX_LAYER);
    }
}
