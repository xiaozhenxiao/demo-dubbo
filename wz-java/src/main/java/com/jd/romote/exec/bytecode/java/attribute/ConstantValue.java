package com.jd.romote.exec.bytecode.java.attribute;

/**
 * final关键字定义的常量值
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantValue extends AttributeInfo {
    private int constantvalueIndex;

    public ConstantValue(int constantvalueIndex) {
        this.constantvalueIndex = constantvalueIndex;
    }

    public int getConstantvalueIndex() {
        return constantvalueIndex;
    }

    public void setConstantvalueIndex(int constantvalueIndex) {
        this.constantvalueIndex = constantvalueIndex;
    }
}
