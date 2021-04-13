package com.silence.letcode.tree;

import java.util.LinkedList;

/**
 * Given a binary tree, determine if it is a valid binary search tree (BST).
 * <p>
 * Assume a BST is defined as follows:
 * <p>
 * The left subtree of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 * @author 李晓冰
 * @date 2020年03月29日
 */
public class LeetCode98 {
    public boolean isValidBST1(TreeNode root) {
        TreeNode prev = null, cur = root;
        LinkedList<TreeNode> stack = new LinkedList<>();

        while (cur != null || stack.size() != 0) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                if (prev != null && prev.val >= cur.val) {
                    return false;
                }
                prev = cur;
                cur = cur.right;
            }
        }
        return true;
    }

    /**
     * 递归实现，不仅要判断当前节点的左节点小于自己还要保证左子树都小于自己
     * @param node   当前判断的节点
     * @param minVal 下边界
     * @param maxVal 上边界
     * @return
     */
    public boolean isValidBST2(TreeNode node, Integer minVal, Integer maxVal) {
        if (node == null) {
            return true;
        }

        if (minVal != null && node.val <= minVal) return false;
        if (maxVal != null && node.val >= maxVal) return false;

        if (!isValidBST2(node.left, minVal, node.val)) return false;
        if (!isValidBST2(node.right, node.val, maxVal)) return false;

        return true;
    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(-2147483648);
        LeetCode98 leetCode98 = new LeetCode98();
        boolean validBST = leetCode98.isValidBST1(treeNode);
        System.out.println(validBST);
        leetCode98.isValidBST2(treeNode, null, null);
    }
}
