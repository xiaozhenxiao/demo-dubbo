package com.jd.romote.exec.bytecode.java;

import com.jd.romote.exec.bytecode.java.attribute.AttributeInfo;

import java.util.ArrayList;

/**
 * 字段表
 * wangzhen23
 * 2017/9/29.
 */
public class FieldInfo {
    private int accessFlags;
    private int nameIndex;
    private int descriptoeIndex;
    private int attributesCount;
    private ArrayList<AttributeInfo> attributes;

    public FieldInfo() {
    }

    public FieldInfo(int accessFlags, int nameIndex, int descriptoeIndex, int attributesCount) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptoeIndex = descriptoeIndex;
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

    public int getDescriptoeIndex() {
        return descriptoeIndex;
    }

    public void setDescriptoeIndex(int descriptoeIndex) {
        this.descriptoeIndex = descriptoeIndex;
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
