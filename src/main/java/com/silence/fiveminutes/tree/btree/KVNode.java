package com.silence.fiveminutes.tree.btree;

/**
 * @author 李晓冰
 * @date 2020年05月19日
 */
public class KVNode<K extends Comparable<K>, V> {
    K k;
    V v;

    public KVNode(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }
}
