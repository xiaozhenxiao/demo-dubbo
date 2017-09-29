package com.jd.romote.exec.bytecode.java;

import com.jd.romote.exec.bytecode.java.attribute.AttributeInfo;
import com.jd.romote.exec.bytecode.java.constant.ConstantPoolInfo;

import java.util.ArrayList;

/**
 * Class文件
 * wangzhen23
 * 2017/9/28.
 */
public class ClassFile {
    //魔数
    private String magic;
    //次版本号
    private int minorVersion;
    //主版本号
    private int majorVersion;
    //常量池计数器
    private int constantPoolCount;
    //常量池
    private ConstantPoolInfo[] constantPool;
    //访问标志
    private int accessFlags;
    //类索引
    private int thisClass;
    //父类索引
    private int superClass;
    //接口计数器
    private int interfacesCount;
    //接口索引集合
    private int[] interfaces;
    //字段计数器
    private int fieldsCount;
    //字段表集合
    private ArrayList<FieldInfo> fields;
    //方法计数器
    private int methodCount;
    //方法表集合
    private ArrayList<MethodInfo> methods;
    //属性计数器
    private int attributesCount;
    //属性表集合
    private ArrayList<AttributeInfo> attributes;

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getConstantPoolCount() {
        return constantPoolCount;
    }

    public void setConstantPoolCount(int constantPoolCount) {
        this.constantPoolCount = constantPoolCount;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public int getThisClass() {
        return thisClass;
    }

    public ConstantPoolInfo[] getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(ConstantPoolInfo[] constantPool) {
        this.constantPool = constantPool;
    }

    public void setThisClass(int thisClass) {
        this.thisClass = thisClass;
    }

    public int getSuperClass() {
        return superClass;
    }

    public void setSuperClass(int superClass) {
        this.superClass = superClass;
    }

    public int getInterfacesCount() {
        return interfacesCount;
    }

    public void setInterfacesCount(int interfacesCount) {
        this.interfacesCount = interfacesCount;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(int[] interfaces) {
        this.interfaces = interfaces;
    }

    public int getFieldsCount() {
        return fieldsCount;
    }

    public void setFieldsCount(int fieldsCount) {
        this.fieldsCount = fieldsCount;
    }

    public ArrayList<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldInfo> fields) {
        this.fields = fields;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(int methodCount) {
        this.methodCount = methodCount;
    }

    public ArrayList<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<MethodInfo> methods) {
        this.methods = methods;
    }

    public int getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(int attributesCount) {
        this.attributesCount = attributesCount;
    }

    public ArrayList<AttributeInfo> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<AttributeInfo> attributes) {
        this.attributes = attributes;
    }
}
