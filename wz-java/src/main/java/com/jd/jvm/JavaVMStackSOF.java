package com.jd.jvm;

/**
 * VM Args: -Xss128k
 * wangzhen23
 * 2017/9/8.
 */
public class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak(){
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Exception {
        JavaVMStackSOF oom = new JavaVMStackSOF();
        try {
            oom.stackLeak();
        } catch (Exception e) {
            System.err.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}
