package com.jd.romote.exec.bytecode.java.attribute;

import java.util.ArrayList;

/**
 * 方法局部变量表
 * wangzhen23
 * 2017/9/29.
 */
public class LocalVariableTable extends AttributeInfo{
    private int localVariableTableLength;
    private ArrayList<LocalVariableInfo> localVariableTable;

    public int getLocalVariableTableLength() {
        return localVariableTableLength;
    }

    public void setLocalVariableTableLength(int localVariableTableLength) {
        this.localVariableTableLength = localVariableTableLength;
    }

    public ArrayList<LocalVariableInfo> getLocalVariableTable() {
        return localVariableTable;
    }

    public void setLocalVariableTable(ArrayList<LocalVariableInfo> localVariableTable) {
        this.localVariableTable = localVariableTable;
    }
}
