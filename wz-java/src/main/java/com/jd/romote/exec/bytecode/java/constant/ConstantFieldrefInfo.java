package com.jd.romote.exec.bytecode.java.constant;

/**
 * 字段的符号引用
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantFieldrefInfo extends ConstantPoolInfo{
    //指向声明字段的类或者接口描述符CONSTANT_Class_info的索引项
    private int classIndex;
    //指向字段描述符CONSTANT_NameAndType_info的索引项
    private int nameAndTypeIndex;

    public ConstantFieldrefInfo(int classIndex, int nameAndTypeIndex) {
        this.tag = 9;
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    public void setNameAndTypeIndex(int nameAndTypeIndex) {
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}
