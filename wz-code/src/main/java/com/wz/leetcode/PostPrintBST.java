package com.wz.leetcode;

/**
 * 题目描述：
 * 二叉树的前序、中序、后序遍历的定义： 前序遍历：对任一子树，先访问跟，然后遍历其左子树，最后遍历其右子树；
 * 中序遍历：对任一子树，先遍历其左子树，然后访问根，最后遍历其右子树；
 * 后序遍历：对任一子树，先遍历其左子树，然后遍历其右子树，最后访问根。
 * 给定一棵二叉树的前序遍历和中序遍历，求其后序遍历（提示：给定前序遍历与中序遍历能够唯一确定后序遍历）。
 * <p>
 * 输入描述:
 * 两个字符串，其长度n均小于等于26。
 * 第一行为前序遍历，第二行为中序遍历。
 * 二叉树中的结点名称以大写字母表示：A，B，C....最多26个结点。
 * 输出描述:
 * 输入样例可能有多组，对于每组测试样例，
 * 输出一行，为后序遍历的字符串。
 */
public class PostPrintBST {

    public static void main(String[] args) {
        String preorder="FDXEAG";
        String inorder="XDEFAG";
        getpost(preorder, inorder);
    }

    static void getpost(String preorder, String inorder)  //根据先序和中序求后序
    {
        int n = preorder.length();  //n为每次遍历数目
        if (n > 0) {
            char root = preorder.charAt(0);   //根结点为先序遍历的第一个
            int i = inorder.indexOf(root);  //中序遍历中根结点的所在下标
            getpost(preorder.substring(1, i+1), inorder.substring(0, i+1)); //左子树
            getpost(preorder.substring(i + 1), inorder.substring(i + 1)); //右子树
            System.out.print(root);
        }
    }

}