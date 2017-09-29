package com.jd.romote.exec.bytecode.java.constant;

/**
 * 双精度浮点型字面量 占用常量池两个长度
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantDoubleInfo extends ConstantPoolInfo {
    private Double bytes;

    public ConstantDoubleInfo(Double bytes) {
        this.tag = 6;
        this.bytes = bytes;
    }

    public Double getBytes() {
        return bytes;
    }

    public void setBytes(Double bytes) {
        this.bytes = bytes;
    }
}
