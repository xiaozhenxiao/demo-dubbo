package com.jd.romote.exec.bytecode.java.constant;

/**
 * 标识方法句柄
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantMethodHandleInfo extends ConstantPoolInfo {
    //值必须在1~9之间（包括1和9），它决定了方法句柄的类型。方法句柄类型的值表示方法句柄的字节码行为
    private int referenceKind;
    //值必须是对常量池的有效索引
    private int referenceIndex;

    public ConstantMethodHandleInfo(int referenceKind, int referenceIndex) {
        this.tag = 15;
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public int getReferenceKind() {
        return referenceKind;
    }

    public void setReferenceKind(int referenceKind) {
        this.referenceKind = referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }

    public void setReferenceIndex(int referenceIndex) {
        this.referenceIndex = referenceIndex;
    }
}
