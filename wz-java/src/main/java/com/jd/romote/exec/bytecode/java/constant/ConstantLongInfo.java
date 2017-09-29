package com.jd.romote.exec.bytecode.java.constant;

/**
 * 长整型字面量 占用常量池两个长度
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantLongInfo extends ConstantPoolInfo {
    private Long bytes;

    public ConstantLongInfo(Long bytes) {
        this.tag = 5;
        this.bytes = bytes;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }
}
