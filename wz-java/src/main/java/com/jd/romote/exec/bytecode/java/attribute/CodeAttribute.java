package com.jd.romote.exec.bytecode.java.attribute;

import java.util.ArrayList;

/**
 * Code属性
 * wangzhen23
 * 2017/9/29.
 */
public class CodeAttribute extends AttributeInfo {
    private int maxStack;
    private int maxLocals;
    private int codeLength;
    private ArrayList<Integer> code;
    private int exceptionTableLength;
    private ArrayList<ExceptionInfo> exceptionTable;
    private int attributesCount;
    private ArrayList<AttributeInfo> attributes;

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public ArrayList<Integer> getCode() {
        return code;
    }

    public void setCode(ArrayList<Integer> code) {
        this.code = code;
    }

    public int getExceptionTableLength() {
        return exceptionTableLength;
    }

    public void setExceptionTableLength(int exceptionTableLength) {
        this.exceptionTableLength = exceptionTableLength;
    }

    public ArrayList<ExceptionInfo> getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(ArrayList<ExceptionInfo> exceptionTable) {
        this.exceptionTable = exceptionTable;
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
