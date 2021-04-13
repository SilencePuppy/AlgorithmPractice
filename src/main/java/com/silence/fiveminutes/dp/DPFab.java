package com.silence.fiveminutes.dp;

import java.util.HashMap;

/**
 * @author 李晓冰
 * @date 2020年02月23日
 */
public class DPFab {

    private HashMap<Integer, Integer> cache = new HashMap<>();

    public int Fab(int i) {

        if (i == 1 || i == 2) {
            return 1;
        }
        if (cache.get(i) != null) {
            return cache.get(i);
        }
        int val = Fab(i - 1) + Fab(i - 2);
        cache.put(i, val);
        return val;
    }

    public int FabIterate(int i) {
        if (i == 1 || i == 2) {
            return 1;
        }
        int prev1 = 1;
        int prev2 = 1;
        int result = 0;
        for (int j = 3; j <= i; j++) {
            result = prev1 + prev2;
            prev1 = prev2;
            prev2 = result;
        }
        return result;
    }

    public static void main(String[] args) {
        DPFab dpFab = new DPFab();
        System.out.println(dpFab.Fab(7));
        System.out.println(dpFab.FabIterate(7));
    }
}
