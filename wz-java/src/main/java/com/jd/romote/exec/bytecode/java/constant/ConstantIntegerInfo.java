package com.jd.romote.exec.bytecode.java.constant;

/**
 * 整型字面量
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantIntegerInfo extends ConstantPoolInfo {
    private Integer bytes;

    public ConstantIntegerInfo(Integer bytes) {
        this.tag = 3;
        this.bytes = bytes;
    }

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }
}
