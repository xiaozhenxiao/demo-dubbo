package com.jd.romote.exec.bytecode.java.constant;

/**
 * 类或接口的符号引用
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantClassInfo extends ConstantPoolInfo {
    //指向全限定名常量项的索引
    private int index;

    public ConstantClassInfo(int index) {
        this.tag = 7;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue(ConstantPoolInfo[] constantPool){
        return ((ConstantUtf8Info)constantPool[this.index]).getBytes();
    }
}
