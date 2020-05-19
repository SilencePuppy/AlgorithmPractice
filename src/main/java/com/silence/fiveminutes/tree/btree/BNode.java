package com.silence.fiveminutes.tree.btree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李晓冰
 * @date 2020年05月19日
 */
public class BNode<K extends Comparable<K>, V> {

    public static final int DEGREE = 3;
    public static final int MIN_K_NUM = DEGREE - 1;
    public static final int MAX_K_NUM = DEGREE * 2 - 1;

    private KVNode<K, V>[] keys;
    private BNode<K, V>[] children;
    private boolean isLeaf;
    private int nKey;
    private BNode<K, V> parent;

    @SuppressWarnings("unchecked")
    BNode(boolean isLeaf) {
        // 预留了一个节点位置，方便插入操作代码实现
        keys = new KVNode[MAX_K_NUM + 1];
        if (!isLeaf) {
            children = new BNode[MAX_K_NUM + 2];
        }
        this.isLeaf = isLeaf;
    }

    private void dispose() {
        for (int i = 0; i < nKey; i++) {
            keys[i] = null;
            if (!isLeaf) {
                children[i] = null;
            }
        }
        if (!isLeaf) {
            children[nKey] = null;
        }
        parent = null;
    }

    boolean isLeaf() {
        return isLeaf;
    }

    boolean isFull() {
        return nKey == keys.length;
    }

    boolean isOverMin() {
        return nKey > MIN_K_NUM;
    }

    boolean isLess() {
        if (parent == null) {
            return false;
        } else {
            return nKey < MIN_K_NUM;
        }
    }

    int getNKey() {
        return nKey;
    }

    BNode<K, V> getParent() {
        return parent;
    }

    KVNode<K, V> getLastKVNode() {
        return getKVNodeByIndex(nKey - 1);
    }

    KVNode<K, V> getFirstKVNode() {
        return getKVNodeByIndex(0);
    }

    KVNode<K, V> getKVNodeByIndex(int index) {
        return keys[index];
    }

    void setKVNodeByIndex(int index, KVNode<K, V> node) {
        keys[index] = node;
    }

    void insertKVNode(int index, KVNode<K, V> kvNode) {
        if (nKey - index > 0) {
            System.arraycopy(keys, index, keys, index + 1, nKey - index);
        }
        keys[index] = kvNode;
        nKey++;
    }

    KVNode<K, V> removeLastKVNode() {
        return removeKVNode(getNKey() - 1);
    }

    KVNode<K, V> removeFirstKVNode() {
        return removeKVNode(0);
    }

    KVNode<K, V> removeKVNode(int index) {
        KVNode<K, V> ret = getKVNodeByIndex(index);
        if (nKey - index - 1 > 0) {
            System.arraycopy(keys, index + 1, keys, index, nKey - index - 1);
        }
        keys[nKey-1] = null;
        nKey--;
        return ret;
    }

    BNode<K, V> getLastChild() {
        return getChildByIndex(nKey);
    }

    BNode<K, V> getFirstChild() {
        return getChildByIndex(0);
    }

    BNode<K, V> removeLastChild() {
        return removeChildByIndex(getNKey());
    }

    BNode<K, V> removeFirstChild() {
        return removeChildByIndex(0);
    }

    private BNode<K, V> removeChildByIndex(int index) {
        BNode<K, V> retChild = children[nKey];
        if (nKey - index > 0) {
            System.arraycopy(children, index + 1, children, index, nKey - index);
        }
        retChild.parent = null;
        children[nKey] = null;
        return retChild;
    }

    BNode<K, V> getChildByIndex(int index) {
        return children[index];
    }

    private void insertChildByIndex(int index, BNode<K, V> node) {
        if (nKey - index + 1 > 0) {
            System.arraycopy(children, index, children, index + 1, nKey - index + 1);
        }
        children[index] = node;
        node.parent = this;
    }

    BNode<K, V> getSuccessor(BNode<K, V> parent, int index) {
        if (parent.isLeaf()) {
            return null;
        }
        BNode<K, V> child = parent.getChildByIndex(index);
        while (!child.isLeaf()) {
            child = child.getChildByIndex(0);
        }
        return child;
    }

    /**
     * @param targetKey 目标key
     * @return 如果找到则返回的是key下标，否则返回的是targetKey可能在的子树节点下标
     */
    int binarySearch(K targetKey) {
        int low = 0, high = nKey - 1;
        while (low <= high) {
            int mid = (high - low) / 2 + low;
            int cmp = keys[mid].getK().compareTo(targetKey);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    BNode<K, V> split() {
        int mid = nKey / 2;
        BNode<K, V> rightNode = new BNode<>(isLeaf);

        if (parent == null) {
            parent = new BNode<>(false);
            parent.children[0] = this;
        }
        if (isLeaf) {
            for (int i = mid + 1; i < nKey; i++) {
                rightNode.keys[i - mid - 1] = keys[i];
                keys[i] = null;
            }
        } else {
            for (int i = mid + 1; i < nKey; i++) {
                rightNode.keys[i - mid - 1] = keys[i];
                rightNode.children[i - mid - 1] = children[i];
                children[i].parent = rightNode;
                keys[i] = null;
                children[i] = null;
            }
            rightNode.children[nKey - mid - 1] = children[nKey];
            children[nKey].parent = rightNode;
            children[nKey] = null;
        }
        rightNode.nKey = nKey - mid - 1;
        rightNode.parent = parent;
        parent.addKAndChild(keys[mid], rightNode);

        nKey = mid;
        keys[mid] = null;
        return parent;
    }

    private void addKAndChild(KVNode<K, V> kvNode, BNode<K, V> child) {
        int i = nKey;
        for (; i > 0; i--) {
            if (keys[i - 1].getK().compareTo(kvNode.getK()) > 0) {
                keys[i] = keys[i - 1];
                if (!isLeaf) {
                    children[i + 1] = children[i];
                }
            } else {
                break;
            }
        }
        keys[i] = kvNode;
        if (!isLeaf) {
            children[i + 1] = child;
            child.parent = this;
        }
        nKey++;
    }

    BNode<K, V> getPreSibling(int inParentIndex) {
        if (inParentIndex == 0) {
            return null;
        }
        return parent.getChildByIndex(inParentIndex - 1);
    }

    BNode<K, V> getSucceSibling(int inParentIndex) {
        if (inParentIndex == parent.getNKey()) {
            return null;
        }
        return parent.getChildByIndex(inParentIndex + 1);
    }

    void borrowFromPreSibling(int inParentIndex, BNode<K, V> preSibling) {
        KVNode<K, V> parentKVNode = parent.getKVNodeByIndex(inParentIndex - 1);
        KVNode<K, V> lastKVNode = preSibling.getLastKVNode();

        // 必须先删除孩子
        if (!isLeaf()) {
            BNode<K, V> lastChild = preSibling.getLastChild();
            insertChildByIndex(0, lastChild);
            preSibling.removeLastChild();
        }

        insertKVNode(0, parentKVNode);
        parent.setKVNodeByIndex(inParentIndex - 1, lastKVNode);
        preSibling.removeLastKVNode();
    }

    void borrowFromSucceSibling(int inParentIndex, BNode<K, V> succeSibling) {
        KVNode<K, V> parentKVNode = parent.getKVNodeByIndex(inParentIndex);
        KVNode<K, V> firstKVNode = succeSibling.getFirstKVNode();

        // 必须先删除孩子
        if (!isLeaf()) {
            BNode<K, V> firstChild = succeSibling.getFirstChild();
            insertChildByIndex(getNKey() + 1, firstChild);
            succeSibling.removeFirstChild();
        }

        insertKVNode(getNKey(), parentKVNode);
        parent.setKVNodeByIndex(inParentIndex, firstKVNode);
        succeSibling.removeFirstKVNode();
    }

    void mergeWithSuccesSibling(int inParentIndex, BNode<K, V> succeSibling) {
        KVNode<K, V> parentKVNode = parent.getKVNodeByIndex(inParentIndex);
        // 先删除孩子链接
        parent.removeChildByIndex(inParentIndex + 1);
        parent.removeKVNode(inParentIndex);
        insertKVNode(getNKey(),parentKVNode);

        if (isLeaf) {
            for (int i = 0; i < succeSibling.getNKey(); i++) {
                keys[nKey + i] = succeSibling.getKVNodeByIndex(i);
            }
        } else {
            for (int i = 0; i < succeSibling.getNKey(); i++) {
                keys[nKey + i] = succeSibling.getKVNodeByIndex(i);
                children[nKey + i + 1] = succeSibling.getChildByIndex(i);
                succeSibling.getChildByIndex(i).parent = this;
            }
            children[nKey + succeSibling.getNKey() + 1] = succeSibling.getLastChild();
            succeSibling.getLastChild().parent = this;
        }
        nKey += succeSibling.getNKey();
        succeSibling.dispose();
        if (parent.getNKey() == 0) {
            parent = null;
        }
    }

    int findInParentIndex() {
        if (parent == null) {
            return -1;
        }
        for (int i = 0; i <= parent.getNKey(); i++) {
            if (parent.getChildByIndex(i) == this) {
                return i;
            }
        }
        return -1;
    }

    List<BNode<K, V>> getAllChildren() {
        List<BNode<K, V>> list = new ArrayList<>();
        if (children != null) {
            for (int i = 0; i <= nKey; i++) {
                list.add(children[i]);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < nKey; i++) {
            str += keys[i].getK();
            if (i != nKey - 1) {
                str += ",";
            }
        }
        str += "]";
        return str;
    }
}
