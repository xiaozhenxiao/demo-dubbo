package com.jd.romote.exec.bytecode.java.constant;

/**
 * 字符串类型字面量
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantStringInfo extends ConstantPoolInfo{
    //指向字符串字面量的索引
    private int index;

    public ConstantStringInfo(int index) {
        this.tag = 8;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
