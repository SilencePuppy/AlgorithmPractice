package com.silence.fiveminutes.skiplist;

import java.util.Random;

/**
 * @author Li Xiaobing
 * @Classname SkipList
 * @date 2021/4/13 10:57
 */
public class SkipList<T extends Comparable<T>> {

    // 添加头指针、为了方便下面的代码实现。一定要有这个头结点，会方便很多
    private SkipListNode<T> header;

    private static final int MAX_LEVEL = 32;

    public SkipList() {
        SkipListNode<T> skipListNode = new SkipListNode<>(MAX_LEVEL, null);
        this.header = skipListNode;
    }

    public void add(T data) {
        int level = randomLevel();

        SkipListNode<T> skipListNode = new SkipListNode<>(level, data);

        SkipListNode<T>[] updatesNode = new SkipListNode[level];

        for (int i = 0; i < level; i++) {
            updatesNode[i] = header;
        }

        SkipListNode<T> node = header;
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {
            while (node.getForwards()[i] != null && node.getForwards()[i].getData().compareTo(data) < 0) {
                node = node.getForwards()[i];
            }
            if (i < level) {
                updatesNode[i] = node;
            }
        }

        for (int i = 0; i < level; i++) {
            skipListNode.getForwards()[i] = updatesNode[i].getForwards()[i];
            updatesNode[i].getForwards()[i] = skipListNode;
        }
    }

    /**
     * 查询方法，
     *
     * @param data
     * @return {@link T}
     * @author Li Xiaobing
     * @date 2021/4/13 14:27
     */
    public T find(T data) {
        SkipListNode<T> node = header;
        int i = MAX_LEVEL - 1;
        // 此处要i能够达到0，这样就可以在最底层链表进行数据比较
        for (; i >= 0; i--) {
            while (node.getForwards()[i] != null && node.getForwards()[i].getData().compareTo(data) < 0) {
                node = node.getForwards()[i];
            }
        }

        if (node.getForwards()[0] != null && node.getForwards()[0].getData().compareTo(data) == 0) {
            return node.getForwards()[0].getData();
        } else {
            return null;
        }
    }

    public void printAll() {
        SkipListNode<T> node = header;
        int i = 1;
        while (node.getForwards()[0] != null) {
            System.out.print(node.getForwards()[0].getData() + " ");
            node = node.getForwards()[0];
            i++;
            if (i % 100 == 0) {
                System.out.println();
            }
        }
    }


    /**
     * 返回值位于1~32之间，且数值越大出现的概率越小
     *
     * @return {@link int}
     * @author Li Xiaobing
     * @date 2021/4/13 11:11
     */
    private int randomLevel() {
        int i = 1;
        Random random = new Random(System.currentTimeMillis());
        for (; i < MAX_LEVEL; i++) {
            if (random.nextInt(2) == 0) {
                break;
            }
        }
        return i;
    }
}
