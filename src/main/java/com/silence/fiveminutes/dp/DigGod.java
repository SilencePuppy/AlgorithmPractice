package com.silence.fiveminutes.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * 有一个国家发现了 5 座金矿，每座金矿的黄金储量不同，需要参与挖掘的工人数也不同。
 * 参与挖矿工人的总数是 10 人。每座金矿要么全挖，要么不挖，不能派出一半人挖取一半金矿。
 * 要求用程序求解出，要想得到尽可能多的黄金，应该选择挖取哪几座金矿？
 * 矿1:200金 3人
 * 矿2:300金 4人
 * 矿3:350金 3人
 * 矿4:400金 5人
 * 矿5:500金 5人
 * 思路：
 * 找状态转移：假设f(n,w) 代表n个矿，w个人时的可挖最大价值,p代表挖当前矿需要的人数，v是当前矿的价值
 * f(n,w) = f(n-1,w) 当w人不够挖第n个矿的时候
 * f(n,w) = max{f(n-1,w),f(n-1,w-p)+v} 如果可以挖当前矿则最优值是，max{不挖当前矿,挖了当前矿后+其余人挖剩余矿的最优值}
 * 上面的条件可以将一个大规模的问题转化为小规模的问题了。
 * 找边界条件：
 * f(n,w) = 0 当n==0或者n==1当时p>w 没有矿，或者只有一个矿但是人手不够
 * f(n,w) = v 当n==1&&p<=w 只有一个矿且人手够
 * @author 李晓冰
 * @date 2020年02月25日
 */
public class DigGod {
    /**
     * 每一个金矿的价值,下标从1开始
     */
    private int gold[];
    /**
     * 每一个金矿需要的人数,下标从1开始
     */
    private int person[];
    /**
     * 缓存，记录计算过的值
     */
    private Map<String, Integer> cache = new HashMap<>();

    public DigGod() {
        this.gold = new int[]{0, 200, 300, 350, 400, 500};
        this.person = new int[]{0, 3, 4, 3, 5, 5};
    }

    /**
     * 求解当拥有goldNum个矿和personNum个人时 能够挖的总价值
     * @param goldNum   矿的数量同时也表示当前打算开挖的一个矿
     * @param personNum 当前人的数量
     * @return 总价值
     */
    public int find(int goldNum, int personNum) {
        // 没有矿
        if (goldNum < 1) {
            return 0;
        }
        // 只有一个矿
        if (goldNum == 1) {
            // 人数够挖这个矿的
            if (personNum >= person[goldNum]) {
                return gold[goldNum];
            } else {
                // 人数不够挖这个矿的
                return 0;
            }
        }

        String key = goldNum + "" + personNum;
        if (cache.get(key) != null) {
            return cache.get(key);
        }

        int maxValue;
        // 要挖的这个矿需要的人数比现有人数大，则不挖这个矿了，最值就是personNum个人挖剩下矿的最多价值
        if (personNum < person[goldNum]) {
            maxValue = find(goldNum - 1, personNum);
        } else {
            // 如果可以挖当前矿，则计算出挖他的最终价值和不挖他的最终价值谁大
            // 这个矿的价值
            int thisGoldValue = gold[goldNum];
            // 挖了次矿后，剩下人挖剩下的矿时挖出来的最大价值
            int remainPersonToDigOtherGoldValue = find(goldNum - 1, personNum - person[goldNum]);
            int value1 = thisGoldValue + remainPersonToDigOtherGoldValue;

            // 如果不挖次矿的价值
            int value2 = find(goldNum - 1, personNum);
            // 取最大值
            maxValue = Math.max(value1, value2);
        }
        cache.put(key, maxValue);
        return maxValue;
    }

    public static void main(String[] args) {
//        DigGod digGod = new DigGod();
//        int i = digGod.find(5, 10);
//        System.out.println(i);
//        System.out.println("----------");
        DigGoldByIterator digGoldByIterator = new DigGoldByIterator(10);
        int i = digGoldByIterator.find();
        System.out.println(i);
    }
}

/**
 * 迭代的方式实现
 */
class DigGoldByIterator {
    /**
     * 每一个金矿的价值,下标从1开始
     */
    private int gold[];
    /**
     * 每一个金矿需要的人数,下标从1开始
     */
    private int person[];

    private int totalPerson;
    /**
     * 缓存，计算过的值,
     * 可以用一维数组实现，（倒着遍历数组哦）
     */
    private int[][] goldMemo;

    public DigGoldByIterator(int totalPerson) {
        this.totalPerson = totalPerson;
        this.gold = new int[]{0, 200, 300, 350, 400, 500};
        this.person = new int[]{0, 3, 4, 3, 5, 5};
        goldMemo = new int[this.gold.length][totalPerson + 1];
    }

    public int find() {
        for (int i = 1; i < this.gold.length; i++) {
            for (int j = 1; j <= this.totalPerson; j++) {
                // 人不够
                if (j < person[i]) {
                    goldMemo[i][j] = goldMemo[i - 1][j];
                } else {
                    int selfValue = gold[i] + goldMemo[i - 1][j - person[i]];
                    int notSelfVale = goldMemo[i - 1][j];
                    goldMemo[i][j] = Math.max(selfValue, notSelfVale);
                }
            }
        }
        return goldMemo[this.gold.length - 1][this.totalPerson];
    }
}