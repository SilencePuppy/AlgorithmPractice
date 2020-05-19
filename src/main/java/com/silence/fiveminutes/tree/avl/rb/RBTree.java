package com.silence.fiveminutes.tree.avl.rb;

/**
 * RBTree
 * 1.节点非黑即红
 * 2.根节点是黑色的
 * 3.叶子节点（指null节点）是黑色
 * 4.红节点如果有孩子节点，则孩子节点一定是黑色（不能连续红节点）
 * 5.每一个节点到其可达的叶子节点的路径上经过的黑节点数量一致（节点的黑高一样）
 * <p>
 * 首先RBTree不算是一种严格的平衡二叉树，因为他是黑节点的高度一致，AVL树太严格虽然查找很快但是对插入删除时牺牲太大。
 * <p>
 * 由以上规则可以推出：
 * 一个节点的父节点是红节点的话其肯定有祖父节点
 * 一个节点如果只有一个孩子节点那么该节点一定是黑节点，且他没有孙子节点（孩子节点是孤家寡人）
 * @author 李晓冰
 * @date 2020年04月11日
 */
public class RBTree<T extends Comparable<T>> {
    private RBTreeNode<T> root;

    public RBTreeNode<T> getRoot() {
        return root;
    }

    /**
     * 搜索数据
     * @param data
     * @return
     */
    public RBTreeNode<T> find(T data) {
        RBTreeNode<T> node = root;
        while (node != null) {
            if (data.compareTo(node.getValue()) > 0) {
                node = node.getRight();
            } else if (data.compareTo(node.getValue()) < 0) {
                node = node.getLeft();
            } else {
                break;
            }
        }
        return node;
    }

    /**
     * 插入操作分析
     * 首先新插入的节点我们规定其为红色节点：因为插入树肯定满足红黑树的规则，那么插入黑节点节后该节点的
     * 父节点的黑高就不一致了（新插入的节点导致一边黑高加一）。
     * <p>
     * 插入操作可能导致红黑树结构被破坏所以我们要对其进行调整。我们将插入节点视为当前处理节点
     * 1.如果插入节点的父节点是黑色，则不需要进行调整
     * 2.如果父节点是红色节点则，需要调整。（叔叔节点指父节点的兄弟节点）
     * 2.1叔叔节点如果是红色。
     * 祖父节点一定是黑节点，那为了保证黑高不变，我们可以将父节点和叔叔节点变成黑色，祖父节点变为红色
     * 这样对于祖父节点这颗子树来说黑高依然保持不变，其对外表现黑高也不变。但是祖父节点变成红色后如果祖父节点
     * 的父节点也是红色的话我们还是需要继续进行调整,所以需要把祖父节点视为当前处理节点然后进行递归（迭代）操作。
     * 直到遇到当前处理节点的父亲是黑色或者到根节点。如果祖父节点就是root节点，那么操作后会把根节点变为红色。
     * 这个时候我们需要再把根节点变成黑色，这个操作是导致RBTree黑高增加的唯一原因。
     * 2.2叔叔节点不是红色（那么在不考虑2.1情况向上迭代的话，叔叔节点一定是空否则的话祖父节点的黑高就不对了）
     * 可以断定当前节点一定有祖父节点且祖父是黑色，这个时候我们为了保证黑高不变可以选择将父节点和祖父节点颜色交互（父亲变黑祖父变红）
     * 然后对祖父进行旋转操作(如果当前处理节点、父亲、祖父节点不在一条线上，需要在变色前先对父亲进行旋转操作)。
     * 总之就一个规则，让黑高保持和插入前一样（除了2.1里面可能存在的黑高增加一的情况）
     * @param data
     */
    public void insert(T data) {
        RBTreeNode<T> p = null;
        RBTreeNode<T> node = root;

        while (node != null) {
            int cmp = node.getValue().compareTo(data);
            if (cmp == 0) {
                return;
            } else {
                p = node;
                if (cmp > 0) {
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            }
        }

        RBTreeNode<T> newNode = new RBTreeNode<>(data);
        newNode.setRed(true);

        newNode.setParent(p);
        if (p == null) {
            root = newNode;
            root.setRed(false);
        } else {
            if (p.getValue().compareTo(data) > 0) {
                p.setLeft(newNode);
            } else {
                p.setRight(newNode);
            }
            fixInsert(newNode);
        }

    }

    /**
     * @param node 当前插入的节点
     */
    @SuppressWarnings("all")
    private void fixInsert(RBTreeNode<T> node) {
        // node不可能是根节点，所以其肯定有parent,但是不停迭代的过程中可能抵达根节点
        RBTreeNode<T> parent = node.getParent();

        // if parent is red then it must have parent
        while (parent != null && parent.isRed()) {
            // 父节点是红色，那么肯定有叔叔节点
            RBTreeNode<T> uncle = getUncle(node);
            // 如果当前节点是新插入的节点则其叔叔节点只能是红或者null,但是如果通过不停的向上迭代则可能出现黑色
            // 如依次插入50 30 70 19 35  15 22 14
            if (uncle == null || uncle.isBlack()) {
                RBTreeNode<T> ancestor = parent.getParent();

                if (parent == ancestor.getLeft()) {
                    boolean isRight = node == parent.getRight();
                    // 不在同一条线上
                    if (isRight) {
                        rotateLeft(parent);
                    }
                    rotateRight(ancestor);

                    if (isRight) {
                        node.setRed(false);
                    } else {
                        parent.setRed(false);
                    }
                } else {
                    boolean isLeft = node == parent.getLeft();
                    if (isLeft) {
                        rotateRight(parent);
                    }
                    rotateLeft(ancestor);

                    if (isLeft) {
                        node.setRed(false);
                    } else {
                        parent.setRed(false);
                    }
                }
                ancestor.setRed(true);
                break;
            } else {
                // uncle is red
                parent.setRed(false);
                uncle.setRed(false);
                parent.getParent().setRed(true);

                node = parent.getParent();
                parent = node.getParent();
                // 此时node可能为根节点，此时parent就是null，这个时候循环迭代之后将root重置为黑色，树的整体黑高也会加1
            }
        }
        // 最后保证一下根节点的颜色，
        root.setRed(false);
    }

    private RBTreeNode<T> getUncle(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();
        // not be null
        RBTreeNode<T> ancestor = parent.getParent();
        assert ancestor != null;

        if (parent == ancestor.getLeft()) {
            return ancestor.getRight();
        } else {
            return ancestor.getLeft();
        }
    }

    /**
     * 删除操作时相比插入操作要麻烦的多。
     * 首先RBTree也是BST,所以删除时先按照BST的删除进行操作，删除后需要针对已删除节点的颜色进行结构修复
     * 以保证红黑树的性质不被破坏。现在我们规定A步奏是找到对应待删除的节点，B步奏是进行调整操作。
     * A: 定义待删除节点为target.
     * 1.target 没有孩子节点（那么target可能是红色也可能是黑色）直接进入步奏B
     * 2.target 有左或右孩子，那么target节点一定是黑色节点，且其孩子节点是红色，此时我们就可以用孩子节点的值
     * 替代当前节点的值，然后将该孩子节点当做target节点。(这个地方和BST有些不同，BST中该节点可能会有子孙节点)
     * 然后进入步奏B。
     * 3.target 有双孩子节点，此时我们按照BST的做法，去找当前target节点的直接后继节点（找右子树的最左孩子），
     * 用后继节点的值替代target的值，该节点定义为target节点。此时又进入了A-1或A-2的情况。
     * 所以针对A情况我们最后都会进入删除最后叶子节点的阶段（非null叶子）。
     * B:target依然是待删除节点，删除操作先修改其孩子节点的父指针和父节点的孩子指针，target自身对parent和child的
     * 引用暂且不删除，等到调整之后再设置为null。
     * 1.如果删除的是红节点则不需修改。
     * 2.如果是黑节点：假定target是父节点的左子树（右子树进行相反操作）。此时左节点删除了那么父节点的黑高就会发生变化，
     * 这个时候我们做的是尽量使父节点的黑高恢复到删除前的状态，这样能够使外界的黑高不受到影响（自己尽量解决问题不要打扰领导）。
     * 我们可以考虑其父节点的右子树是否有红节点，如果有的话我们是否可以通过旋转和变色来让左边向右边借一个黑节点过来。
     * 2.1 如果兄弟节点是红色。
     * 删除前该树肯定是一个红黑树，那么由于兄弟是红色，target是黑色，那么可以断定兄弟节点肯定有两个孩子且都是黑色。
     * 父节点的颜色肯定是黑色。此时我们将兄弟节点变成黑色，兄弟节点左孩子变为红色，然后对父节点进行左旋。
     * 这样就能保证删除并旋转后左边的黑高不变，右边的黑高也不变。变化的是兄弟由红变成黑色，而左边则由删除后没有黑节点变成
     * 又有了黑节点（兄弟左孩子变成红色是旋转后不增加左边黑高）。这就是借红节点。
     * 2.2 如果兄弟节点是黑色。（那么我们无法确定父节点的颜色，但是兄弟节点如果有孩子的话一定是红孩儿）
     * 2.2.1 兄弟的右孩子存在是红色的（左孩子有没有无所谓）
     * 此时我们就可以将兄弟的右孩儿变成黑的，兄弟变成父亲的颜色，父亲变成黑的，然后对父亲进行左旋。
     * 这样就通过向兄弟借红维持了黑高的不变。
     * 2.2.2 兄弟的左孩子是红的且没有右孩子。
     * 此时我们需要先将左孩子变成黑色，兄弟变成红色，然后对兄弟进行右旋转。这样我们就回到了2.2.1的情况。
     * 2.2.3 兄弟没有孩子。
     * 由于兄弟没孩子，所以我们也就无法向兄弟借红节点了，而父节点的左孩子被删除导致左边黑高少一，我们的做法
     * 就是将兄弟变成红色，这样父亲的黑高就统一了，但是以父亲为孩子节点的那个树的黑高依然不平衡。这个时候我们就
     * 把父亲当做当前处理节点，看能否从父亲的兄弟节点借一个红节点过来。直到能借过来（符合2.2.1或2.2.3的情况）。或者
     * 到当前处理节点是根节点（node.parent==null，导致树的黑高减一），或者当前节点是红节点了（这个时候我们就将当前
     * 节点由红色变成黑色，以此保证这边的子树黑高不变时）。
     * @param data
     */
    public void delete(T data) {
        // 首先找到待删除的节点
        RBTreeNode<T> node = find(data);
        // 没有直接返回
        if (node == null) {
            return;
        }

        if (node.getLeft() != null && node.getRight() != null) {
            // 找到后继节点，并用后继节点替代自己（可以理解为状态的转移）
            RBTreeNode<T> child = successor(node);
            node.setValue(child.getValue());
            node = child;
        }

        RBTreeNode<T> replaceNode = node.getLeft() == null ? node.getRight() : node.getLeft();

        // BST可不是这样写的。RBT才能这样玩，因为可以断定replaceNode不是null的情况下他肯定没孩子
        if (replaceNode != null) {
            node.setValue(replaceNode.getValue());
            node = replaceNode;
        }

        // 让父节点和自己断开连接，但是在调整前还要保持自己能找到父亲
        if (node.getParent() == null) {
            root = null;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(null);
        } else {
            node.getParent().setRight(null);
        }
        if (root != null) {
            fixDelete(node);
            node.setParent(null);
        }
    }

    private RBTreeNode<T> successor(RBTreeNode<T> node) {
        RBTreeNode<T> n = node.getRight();
        while (n.getLeft() != null) {
            n = n.getLeft();
        }
        return n;
    }

    private void fixDelete(RBTreeNode<T> node) {
        RBTreeNode<T> p = node;
        // 当前节点不是根节点且不是红色
        while (p.getParent() != null && p.isBlack()) {
            // 先进父节点暂存，因为涉及到旋转操作会导致指针变化
            RBTreeNode<T> parent = p.getParent();
            RBTreeNode<T> brother = getBrother(p);
            // 左子树（只能通过值来比较是左子树还是右，因为最开始的时候我们已经将node的父亲的孩子指针修改了）
            if (p.getValue().compareTo(parent.getValue()) < 0) {
                // 兄弟是红色 B-2.1场景
                if (brother.isRed()) {
                    brother.setRed(false);
                    brother.getLeft().setRed(true);
                    rotateLeft(parent);
                    break;
                } else {
                    // 兄弟是黑色且没有孩子 B-2.2.3场景
                    if (brother.getLeft() == null && brother.getRight() == null) {
                        brother.setRed(true);
                        p = parent;
                    } else {
                        // 兄弟右孩子节点 进入B-2.2场景

                        //兄弟右孩子是红色 进入B-2.2.1场景
                        if (brother.getRight() != null && brother.getRight().isRed()) {
                            brother.setRed(parent.isRed());
                            parent.setRed(false);
                            brother.getRight().setRed(false);
                            rotateLeft(parent);
                            break;
                        } else {
                            //兄弟只有左孩子 进入B-2.2.2场景
                            brother.getLeft().setRed(false);
                            brother.setRed(true);
                            rotateRight(brother);
                            // 旋转后重新进行一次迭代，因为此时brother节点已经变化了
                        }
                    }
                }
            } else {
                if (brother.isRed()) {
                    brother.setRed(true);
                    brother.getRight().setRed(true);
                    rotateRight(parent);
                    break;
                } else {
                    if (brother.getLeft() == null && brother.getRight() == null) {
                        brother.setRed(true);
                        p = p.getParent();
                    } else {
                        if (brother.getLeft() != null && brother.getLeft().isRed()) {
                            brother.setRed(parent.isRed());
                            parent.setRed(false);
                            brother.getLeft().setRed(false);
                            rotateRight(parent);
                        } else {
                            brother.setRed(true);
                            brother.getRight().setRed(false);
                        }
                    }
                }
            }
        }
        if (p.isRed()) {
            p.setRed(false);
        }
        root.setRed(false);
    }

    private RBTreeNode<T> getBrother(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();
        if (node.getValue().compareTo(parent.getValue()) < 0) {
            return parent.getRight();
        } else {
            return parent.getLeft();
        }
    }

    private void rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> rightChild = node.getRight();

        node.setRight(rightChild.getLeft());
        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(node);
        }

        rightChild.setParent(node.getParent());
        // node is root
        if (node.getParent() == null) {
            root = rightChild;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(rightChild);
        } else {
            node.getParent().setRight(rightChild);
        }
        node.setParent(rightChild);
        rightChild.setLeft(node);
    }

    private void rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> leftChild = node.getLeft();

        node.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(node);
        }

        if (node.getParent() == null) {
            root = leftChild;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(leftChild);
        } else {
            node.getParent().setRight(leftChild);
        }

        node.setParent(leftChild);
        leftChild.setRight(node);
    }

    @SuppressWarnings("all")
    public void printTree(RBTreeNode<T> root) {
        java.util.LinkedList<RBTreeNode<T>> queue = new java.util.LinkedList<RBTreeNode<T>>();
        java.util.LinkedList<RBTreeNode<T>> queue2 = new java.util.LinkedList<RBTreeNode<T>>();
        if (root == null) {
            return;
        }
        queue.add(root);
        boolean firstQueue = true;

        while (!queue.isEmpty() || !queue2.isEmpty()) {
            java.util.LinkedList<RBTreeNode<T>> q = firstQueue ? queue : queue2;
            RBTreeNode<T> n = q.poll();

            if (n != null) {
                String pos = n.getParent() == null ? "" : (n == n.getParent().getLeft()
                        ? " LE" : " RI");
                String pstr = n.getParent() == null ? "" : n.getParent().toString();
                String cstr = n.isRed() ? "R" : "B";
                cstr = n.getParent() == null ? cstr : cstr + " ";
                System.out.print(n + "(" + (cstr) + pstr + (pos) + ")" + "\t");
                if (n.getLeft() != null) {
                    (firstQueue ? queue2 : queue).add(n.getLeft());
                }
                if (n.getRight() != null) {
                    (firstQueue ? queue2 : queue).add(n.getRight());
                }
            } else {
                System.out.println();
                firstQueue = !firstQueue;
            }
        }
    }
}
