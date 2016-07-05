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
                        //左旋转
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
            Entry<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
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
