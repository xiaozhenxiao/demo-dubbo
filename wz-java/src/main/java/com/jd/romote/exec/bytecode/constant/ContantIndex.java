package com.jd.romote.exec.bytecode.constant;

/**
 * 常量池中常量的内容
 * wangzhen23
 * 2017/9/23.
 */
public class ContantIndex {
    //符号引用是否已被解析为字符串
    private boolean isAnalysis = true;
    private int tag;
    private String value;

    public ContantIndex(boolean isAnalysis, int tag, String value) {
        this.isAnalysis = isAnalysis;
        this.tag =tag;
        this.value = value;
    }

    public ContantIndex(int tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public boolean isAnalysis() {
        return isAnalysis;
    }

    public void setAnalysis(boolean analysis) {
        isAnalysis = analysis;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
