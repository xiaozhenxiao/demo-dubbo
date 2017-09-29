package com.jd.romote.exec.bytecode.java.constant;

/**
 * UTF8编码的字符串
 * wangzhen23
 * 2017/9/28.
 */
public class ConstantUtf8Info extends ConstantPoolInfo{
    private int length;
    //长度为length的UTF-8编码的字符串
    private String bytes;

    public ConstantUtf8Info() {
        this.tag = 1;
    }

    public ConstantUtf8Info(int length, String bytes) {
        this.tag = 1;
        this.length = length;
        this.bytes = bytes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }
}
