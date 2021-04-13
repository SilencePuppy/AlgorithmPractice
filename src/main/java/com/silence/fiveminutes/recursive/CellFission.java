package com.silence.fiveminutes.recursive;

import java.util.HashMap;

/**
 * 细胞分裂
 * 有一个细胞 每一个小时分裂一次，一次分裂一个子细胞，第三个小时后会死亡（第三小时是死亡的同时分裂出一个细胞）。
 * 那么n个小时候有多少细胞？
 * 将细胞定义为三个状态，A,B,C分别对应1小时，2小时，3小时。
 * 每一种状态数量依赖与上一时刻的父状态的数量
 * f(n) = fa(n)+fb(n)+fc(n)  第n时刻的数量是a,b,c种状态在该时的数量总和
 * fa(n) = fa(n-1)+fb(n-1)+fc(n-1) n时刻a状态的数量是上一时刻a,b,c三个状态数量的总和
 * fb(n) = fa(n-1)
 * fc(n) = fb(n-1)
 * @author 李晓冰
 * @date 2020年02月24日
 */
public class CellFission {

    private HashMap<Integer, Integer> aMemo = new HashMap<>();
    private HashMap<Integer, Integer> bMemo = new HashMap<>();
    private HashMap<Integer, Integer> cMemo = new HashMap<>();

    public int f(int n) {
        return fa(n) + fb(n) + fc(n);
    }

    private int fa(int n) {
        if (n == 1) {
            return 1;
        }

        if (aMemo.get(n) != null) {
            return aMemo.get(n);
        }
        int amount = fa(n - 1) + fb(n - 1) + fc(n - 1);
        aMemo.put(n, amount);
        return amount;
    }

    private int fb(int n) {
        if (n == 1) {
            return 0;
        }
        if (bMemo.get(n) != null) {
            return bMemo.get(n);
        }
        int amount = fa(n - 1);
        bMemo.put(n, amount);
        return amount;
    }

    private int fc(int n) {
        if (n == 1 || n == 2) {
            return 0;
        }

        if (cMemo.get(n) != null) {
            return cMemo.get(n);
        }

        int amount = fb(n - 1);
        cMemo.put(n, amount);
        return amount;
    }

    public static void main(String[] args) {
        CellFission fission = new CellFission();
        int count = fission.f(60);
        System.out.println(count);
    }
}
