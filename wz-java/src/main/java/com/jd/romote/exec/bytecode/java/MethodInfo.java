package com.jd.romote.exec.bytecode.java;

import com.jd.romote.exec.bytecode.java.attribute.AttributeInfo;

import java.util.ArrayList;

/**
 * 方法表
 * wangzhen23
 * 2017/9/29.
 */
public class MethodInfo {
    private int accessFlags;
    private int nameIndex;
    private int descriptorIndex;
    private int attributesCount;
    private ArrayList<AttributeInfo> attributes;

    public MethodInfo() {
    }

    public MethodInfo(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
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
