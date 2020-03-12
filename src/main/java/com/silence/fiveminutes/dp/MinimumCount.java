package com.silence.fiveminutes.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李晓冰
 * @date 2020年02月23日
 */
public class MinimumCount {
    private int[] icons = {1, 2, 5};

    public int minimumCountRecursive(int amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }

        int iconCounts = Integer.MAX_VALUE;
        for (int i = 0; i < icons.length; i++) {
            int childLeast = minimumCountRecursive(amount - icons[i]);
            if (childLeast == -1) {
                continue;
            }
            iconCounts = Math.min(iconCounts, childLeast);
        }
        if (iconCounts == Integer.MAX_VALUE) {
            return -1;
        }
        return iconCounts + 1;
    }

    private Map<Integer, Integer> cache = new HashMap<>();

    public int minimumCountRecursiveWithCache(int amount) {
        if (cache.get(amount) != null) {
            return cache.get(amount);
        }
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }

        Integer result = Integer.MAX_VALUE;
        for (int i = 0; i < icons.length; i++) {
            int childIconCount = minimumCountRecursiveWithCache(amount - icons[i]);
            if (childIconCount < 0) {
                continue;
            }
            result = Math.min(childIconCount, result);
        }
        if (result == Integer.MAX_VALUE) {
            return -1;
        }
        return result + 1;
    }

    public int minimumCountDP(int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 0; i < amount + 1; i++) {
            dp[i] = amount + 1;
        }
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            for (int j = 0; j < icons.length; j++) {
                if (i - icons[j] < 0) {
                    continue;
                }
                int min = Math.min(dp[i - icons[j]] + 1, dp[i]);
                dp[i] = min;
            }
        }
        return dp[amount];
    }

    public static void main(String[] args) {
        MinimumCount count = new MinimumCount();
        int minimumCount = count.minimumCountRecursive(11);
        System.out.println(minimumCount);
        minimumCount = count.minimumCountRecursiveWithCache(11);
        System.out.println(minimumCount);
        minimumCount = count.minimumCountDP(11);
        System.out.println(minimumCount);
    }
}
