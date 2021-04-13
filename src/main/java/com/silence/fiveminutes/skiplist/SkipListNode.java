package com.silence.fiveminutes.skiplist;

import lombok.Data;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Li Xiaobing
 * @Classname SkipListNode
 * @date 2021/4/13 10:53
 */
@Data
public class SkipListNode<T extends Comparable<T>> {

    private int maxLevel;

    private SkipListNode<T>[] forwards;

    private T data;

    private SkipListNode<T> prev;

    public SkipListNode(int maxLevel, T data) {
        this.maxLevel = maxLevel;
        this.data = data;

        initSelf();
    }

    @SuppressWarnings("all")
    private void initSelf() {
        forwards = new SkipListNode[this.maxLevel];
    }

    public static void main(String[] args) {
        SkipList<Long> skipList =new SkipList<>();
        Random random =new Random();
        for (int i = 0; i < 10000; i++) {
            int data = random.nextInt(10000);
            System.out.print(data+"  ");
            if (i % 100 == 0) {
                System.out.println();
            }
            skipList.add(Long.valueOf(data));
        }

        Scanner scanner =new Scanner(System.in);
        String target = scanner.nextLine();
        Long aLong = skipList.find(Long.valueOf(target));
        System.out.println(aLong);
    }
}
