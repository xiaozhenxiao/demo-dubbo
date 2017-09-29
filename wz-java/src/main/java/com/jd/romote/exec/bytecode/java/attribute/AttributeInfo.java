package com.jd.romote.exec.bytecode.java.attribute;

/**
 * 字段表
 * wangzhen23
 * 2017/9/29.
 */
public class AttributeInfo {
    //属性名称描述符，指向CONSTANT_Utf8_info
    private int attributeNameIndex;
    private int attributeLength;

    public int getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public void setAttributeNameIndex(int attributeNameIndex) {
        this.attributeNameIndex = attributeNameIndex;
    }

    public int getAttributeLength() {
        return attributeLength;
    }

    public void setAttributeLength(int attributeLength) {
        this.attributeLength = attributeLength;
    }
}
