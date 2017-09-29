package com.jd.romote.exec.bytecode.java.constant;

/**
 * 类中方法的符号引用
 * wangzhen23
 * 2017/9/29.
 */
public class ConstantMethodrefInfo extends ConstantPoolInfo {
    //指向声明方法的类描述符CONSTANT_Class_info的索引项
    private int classIndex;
    //指向名称及类型描述符CONSTANT_NameAndType的索引项
    private int nameAndTypeIndex;

    public ConstantMethodrefInfo(int classIndex, int nameAndTypeIndex) {
        this.tag = 10;
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

    public String getClassIndexVaule(ConstantPoolInfo[] constantPool){
        return ((ConstantClassInfo)constantPool[this.classIndex]).getValue(constantPool);
    }
    public String getNameIndexValue(ConstantPoolInfo[] constantPool) {
        return ((ConstantNameAndTypeInfo)constantPool[this.nameAndTypeIndex]).getNameIndexValue(constantPool);
    }
    public String getTypeIndexValue(ConstantPoolInfo[] constantPool) {
        return ((ConstantNameAndTypeInfo)constantPool[this.nameAndTypeIndex]).getTypeIndexValue(constantPool);
    }
}
