package com.jd.romote.exec.bytecode.java.attribute;

/**
 * Exception属性
 * wangzhen23
 * 2018/7/26.
 */
public class ExceptionAttribute extends AttributeInfo{
    private int numberOfExceptions;
    private int[] exceptionIndexTable;

    public int getNumberOfExceptions() {
        return numberOfExceptions;
    }

    public void setNumberOfExceptions(int numberOfExceptions) {
        this.numberOfExceptions = numberOfExceptions;
    }
}
