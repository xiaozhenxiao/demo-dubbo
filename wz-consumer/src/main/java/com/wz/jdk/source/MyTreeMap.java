package com.wz.jdk.source;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by wangzhen on 2016-07-04.
 */
public class MyTreeMap<K, V> {
    private final Comparator<? super K> comparator;

    private transient Entry<K,V> root;

    private transient int size = 0;

    private transient int modCount = 0;
    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    public MyTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }
    public V put(K key, V value) {
        Entry<K,V> t = root;
        if (t == null) {
            compare(key, key); // type (and possibly null) check

            root = new Entry<K,V>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Entry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        else {
            if (key == null)
                throw new NullPointerException();
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        Entry<K,V> e = new Entry<K,V>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }
    private void fixAfterInsertion(Entry<K,V> x) {
        x.color = RED;//新增节点的颜色为红色

        //循环 直到 x不是根节点，且x的父节点不为红色
        while (x != null && x != root && x.parent.color == RED) {
            //如果X的父节点（P）是其父节点的父节点（G）的左节点
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                //获取X的叔节点(U)
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    //将X的父节点（P）设置为黑色
                    setColor(parentOf(x), BLACK);
                    //将X的叔节点（U）设置为黑色
                    setColor(y, BLACK);
                    //将X的父节点的父节点（G）设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    //将X的父节点的父节点（G)赋值给X,循环处理
                    x = parentOf(parentOf(x));
                } else {//如果X的叔节点（U为黑色）；这里会存在两种情况（情况四、情况五）
                    //如果X节点为其父节点（P）的右子树，则进行左旋转（情况四）
                    if (x == rightOf(parentOf(x))) {
                        //将X的父节点作为X
                        x = parentOf(x);
                        //左旋转,转变为情况五，即新增节点为父节点的左节点
                        rotateLeft(x);
                    }
                    //（情况五）
                    //将X的父节点（P）设置为黑色
                    setColor(parentOf(x), BLACK);
                    //将X的父节点的父节点（G）设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    //以X的父节点的父节点（G）为中心右旋转
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                //如果X的父节点（P）是其父节点的父节点（G）的右节点
                //获取X的叔节点（U）
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                //如果X的叔节点（U） 为红色（情况三）
                if (colorOf(y) == RED) {
                    //将X的父节点（P）设置为黑色
                    setColor(parentOf(x), BLACK);
                    //将X的叔节点（U）设置为黑色
                    setColor(y, BLACK);
                    //将X的父节点的父节点（G）设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    //将X的父节点的父节点（G)赋值给X,循环处理
                    x = parentOf(parentOf(x));
                } else {//如果X的叔节点（U为黑色）；这里会存在两种情况（情况四、情况五）
                    //如果X节点为其父节点（P）的左子树，则进行右旋转（情况四）
                    if (x == leftOf(parentOf(x))) {
                        //将X的父节点作为X
                        x = parentOf(x);
                        //右旋转
                        rotateRight(x);
                    }
                    //（情况五）
                    //将X的父节点（P）设置为黑色
                    setColor(parentOf(x), BLACK);
                    //将X的父节点的父节点（G）设置红色
                    setColor(parentOf(parentOf(x)), RED);
                    //以X的父节点的父节点（G）为中心左旋转
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }
    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }
    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }
    private void rotateLeft(Entry<K,V> p) {
        if (p != null) {
            //获取P的右子节点，其实这里就相当于新增节点N（情况四而言）
            Entry<K,V> r = p.right;
            //将R的左子树设置为P的右子树
            p.right = r.left;
            //若R的左子树不为空，则将P设置为R左子树的父亲
            if (r.left != null)
                r.left.parent = p;
            //将P的父亲设置R的父亲
            r.parent = p.parent;
            //如果P的父亲为空，则将R设置为跟节点
            if (p.parent == null)
                root = r;
            //如果P为其父节点（G）的左子树，则将R设置为P父节点(G)左子树
            else if (p.parent.left == p)
                p.parent.left = r;
            //否则R设置为P的父节点（G）的右子树
            else
                p.parent.right = r;
            //将P设置为R的左子树
            r.left = p;
            //将R设置为P的父节点
            p.parent = r;
        }
    }

    public V remove(Object key) {
        Entry<K,V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }
    static <K,V> MyTreeMap.Entry<K,V> successor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    private void deleteEntry(Entry<K,V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }
    private void fixAfterDeletion(Entry<K,V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry<K,V> sib = rightOf(parentOf(x));

                //兄为红的情况
                //兄sib 为红色节点,则父节点和兄的孩子节点都为黑色
                if (colorOf(sib) == RED) {
                    //设置兄为黑色
                    setColor(sib, BLACK);
                    //设置父为红色
                    setColor(parentOf(x), RED);
                    //以父为中心，左旋转
                    rotateLeft(parentOf(x));
                    //此时，x的兄sib为黑色,由此转换为了兄为黑色的情况
                    sib = rightOf(parentOf(x));
                }

                //兄为黑的情况
                //兄的左右孩子都为黑
                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    //因为x的分支由于删除，少了一个黑色节点，所以将兄分支的兄节点设置为红色，保证了各分支黑色节点的相同
                    //设置兄为红
                    setColor(sib, RED);
                    //x指向父节点,循环递归向上处理
                    x = parentOf(x);
                } else {
                    //兄的右孩子为黑,左孩子为红
                    if (colorOf(rightOf(sib)) == BLACK) {
                        //设置兄的左孩子为黑,经过右旋后成为x的兄弟节点
                        setColor(leftOf(sib), BLACK);
                        //设置兄为红----A
                        setColor(sib, RED);
                        //以兄为中心右旋
                        rotateRight(sib);
                        //兄指向x的新兄弟节点(此时兄的右节点A为红色)
                        sib = rightOf(parentOf(x));
                    }
                    //兄的右孩子为红的情况,左孩子为红或黑都可以
                    //设置兄为父的颜色,即交换父兄的颜色
                    setColor(sib, colorOf(parentOf(x)));
                    //设置父为黑,即兄的颜色
                    setColor(parentOf(x), BLACK);
                    //设置兄的右孩子为黑色
                    setColor(rightOf(sib), BLACK);
                    //对父进行左旋
                    rotateLeft(parentOf(x));
                    //此时已达到平衡,将x指向root,结束循环
                    x = root;
                }
            } else { // symmetric 对称的,左右互换
                Entry<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, BLACK);
    }
    final Entry<K,V> getEntry(Object key) {
        // Offload comparator-based version for sake of performance
        if (comparator != null)
            return getEntryUsingComparator(key);
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }
    final Entry<K,V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }
    /** From CLR */
    private void rotateRight(Entry<K,V> p) {
        if (p != null) {
            Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }
    private static <K,V> boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }
    private static <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }
    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        boolean color = BLACK;

        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }
    static final boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }
}
