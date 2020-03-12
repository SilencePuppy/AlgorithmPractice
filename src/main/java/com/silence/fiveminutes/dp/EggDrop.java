package com.silence.fiveminutes.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * 面试经典问题：
 * 你面前有一栋从1到N共N层的楼，然后给你K个鸡蛋（K至少为 1）。
 * 现在确定这栋楼存在楼层0 <= F <= N，在这层楼将鸡蛋扔下去，鸡蛋恰好没摔碎（高于F的楼层都会碎，低于F的楼层都不会碎，称为临界点）。
 * 现在问你，最坏情况下，你至少要扔几次鸡蛋，才能确定这个楼层F呢？
 * 题目解析：
 * 首先要明白什么是最坏情况。
 * 如果问你最好情况，那代码都不用写了，为啥？
 * 因为最好情况就是不管你有多少层，我可以从第一层开始尝试，结果碎了，那上面的N-1层肯定都得碎。那最高情况的最少次数就是1
 * 或者是从N层开始尝试，结果没碎，那么下面的N-1层肯定也都不会碎。这种尝试次数也是1。显然这两种情况不是要考察的。
 * 而是，当我们在第i层台阶尝试扔鸡蛋后会有两种结果，碎或者不碎。碎了的话我就要去在1到i-1层台阶找那个临界点，没碎的话就要
 * 去i+1到N层去找临界点。而鸡蛋到底是碎还是不碎呢？由这两种情况操作步骤最多的一个来决定，那这就是最坏情况。而且我们要在最坏的情况下找到N层楼K个鸡蛋的最少尝试次数。
 *
 * 边界情况：
 * 当N=0的时候尝试次数就是0。
 * 当K=1只有一个鸡蛋的时候我们只能从第一层开始一个一个的尝试N次(最坏情况)。
 * 状态转移
 * 假设我们在第i层上进行试验，如果碎了，则我们下一步的测试就是dp(k-1,i-1),没碎则dp(k,N-i)
 * 那么dp(k,i) = Math.max(dp(k-1,i-1),dp(k,N-i))+1 使用max是因为要求的是最坏情况，+1是代表当前层的测试。
 * 又因为我们有N层，所以要把这N层都尝试一遍取最小值，也就是 1<=i<=N
 * 时间复杂度，单个函数的执行复杂度*子状态个数，单个函数是指抛去递归调用处，因为有for循环所以是O(n),使用了备忘录，所以子状态计算次数是K*N个
 * 时间复杂度就是：O(K*N^2)
 * 空间复杂度就是：O(K*N)
 *
 * @author 李晓冰
 * @date 2020年02月26日
 */
public class EggDrop {

    public Map<String, Integer> memo = new HashMap<>();

    public int maxDrop(int eggNum, int stepNum) {
        if (stepNum == 0) {
            return 0;
        }
        if (eggNum == 1) {
            return stepNum;
        }
        String key = eggNum + "" + stepNum;
        if (memo.get(key) != null) {
            return memo.get(key);
        }

        int result = Integer.MAX_VALUE;
        for (int i = 1; i <= stepNum; i++) {
            int otherResult = Math.max(maxDrop(eggNum, stepNum - i), maxDrop(eggNum - 1, i - 1));
            result = Math.min(result, otherResult + 1);
        }
        memo.put(key, result);
        return result;
    }


    public static void main(String[] args) {
        EggDrop eggDrop = new EggDrop();
        System.out.println(eggDrop.maxDrop(2, 100));
    }
}
