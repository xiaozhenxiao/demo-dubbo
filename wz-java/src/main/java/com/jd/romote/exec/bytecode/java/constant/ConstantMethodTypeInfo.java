package com.jd.romote.exec.bytecode.java.constant;

/**
 * 标识方法类型
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantMethodTypeInfo extends ConstantPoolInfo {
    //值必须是对常量池的有效索引，常量池在该索引处的项是CONSTANT_Utf8_info结构，表示方法的描述符
    private int descreptorIndex;

    public ConstantMethodTypeInfo(int descreptorIndex) {
        this.tag = 16;
        this.descreptorIndex = descreptorIndex;
    }

    public int getDescreptorIndex() {
        return descreptorIndex;
    }

    public void setDescreptorIndex(int descreptorIndex) {
        this.descreptorIndex = descreptorIndex;
    }

    public String getDescreptorVaule(ConstantPoolInfo[] constantPool) {
        return ((ConstantUtf8Info) constantPool[this.descreptorIndex]).getBytes();
    }
}
