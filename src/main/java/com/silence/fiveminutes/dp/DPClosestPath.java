package com.silence.fiveminutes.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李晓冰
 * @date 2020年02月23日
 */
public class DPClosestPath {
    private int[][] trangle = {{2}, {3, 4}, {6, 5, 7}, {4, 1, 8, 3}};

    public int closePathRecursion(int i, int j) {
        if (i == trangle.length) {
            return 0;
        }
        int leftPath = closePathRecursion(i + 1, j);
        int rightPath = closePathRecursion(i + 1, j + 1);

        return Math.min(leftPath, rightPath) + trangle[i][j];
    }

    private Map<Integer, Integer> memo = new HashMap<>();

    public int closePathRecursionWithMemo(int i, int j) {
        if (i == trangle.length) {
            return 0;
        }
        String key = i + "" + j;
        if (memo.get(key) != null) {
            return memo.get(key);
        }

        int leftPath = closePathRecursion(i + 1, j);
        int rightPath = closePathRecursion(i + 1, j + 1);
        return Math.min(leftPath, rightPath) + trangle[i][j];
    }

    public int closePathDP() {
        int initLength = trangle[trangle.length - 1].length;
        int[] dp = new int[initLength];
        for (int i = 0; i < initLength; i++) {
            dp[i] = trangle[trangle.length - 1][i];
        }

        for (int i = trangle.length - 2; i >= 0; i--) {
            for (int j = 0; j < trangle[i].length; j++) {
                dp[j] = Math.min(dp[j], dp[j + 1]) + trangle[i][j];
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        DPClosestPath path = new DPClosestPath();
        int pathRecursion = path.closePathRecursion(0, 0);
        System.out.println(pathRecursion);
        pathRecursion = path.closePathRecursionWithMemo(0, 0);
        System.out.println(pathRecursion);
        pathRecursion = path.closePathDP();
        System.out.println(pathRecursion);
    }
}
