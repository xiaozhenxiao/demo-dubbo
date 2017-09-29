package com.jd.romote.exec.bytecode.java.constant;

/**
 * 字段或方法的部分符号引用
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantNameAndTypeInfo extends ConstantPoolInfo {
    //指向该字段或方法名称常量项的索引
    private int nameIndex;
    //指向该字段或方法描述符常量项的索引
    private int typeIndex;

    public ConstantNameAndTypeInfo(int nameIndex, int typeIndex) {
        this.tag = 12;
        this.nameIndex = nameIndex;
        this.typeIndex = typeIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }
}
