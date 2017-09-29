package com.jd.romote.exec.bytecode.java.constant;

/**
 * 标识一个动态方法调用点
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantInvokeDynamicInfo extends ConstantPoolInfo{
    //值必须是对当前Class文件中引导方法表的bootstrap_methods[]数组的有效索引
    private int bootstarpMethodAttrIndex;
    //值必须是对当前常量池的有效索引，常量池在该索引处的项必须是CONSTANT_NameAndType_info结构，标识方法名称和方法描述符
    private int NameAndTypeIndex;

    public ConstantInvokeDynamicInfo(int bootstarpMethodAttrIndex, int nameAndTypeIndex) {
        this.tag = 18;
        this.bootstarpMethodAttrIndex = bootstarpMethodAttrIndex;
        NameAndTypeIndex = nameAndTypeIndex;
    }

    public int getBootstarpMethodAttrIndex() {
        return bootstarpMethodAttrIndex;
    }

    public void setBootstarpMethodAttrIndex(int bootstarpMethodAttrIndex) {
        this.bootstarpMethodAttrIndex = bootstarpMethodAttrIndex;
    }

    public int getNameAndTypeIndex() {
        return NameAndTypeIndex;
    }

    public void setNameAndTypeIndex(int nameAndTypeIndex) {
        NameAndTypeIndex = nameAndTypeIndex;
    }
}
