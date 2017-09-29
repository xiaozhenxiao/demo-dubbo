package com.jd.romote.exec.bytecode.java.constant;

/**
 * 浮点型字面量
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantFloatInfo extends ConstantPoolInfo {
    private Float bytes;

    public ConstantFloatInfo(Float bytes) {
        this.tag = 4;
        this.bytes = bytes;
    }

    public Float getBytes() {
        return bytes;
    }

    public void setBytes(Float bytes) {
        this.bytes = bytes;
    }
}
