package com.jd.romote.exec.bytecode.java.attribute;

/**
 * 异常表
 * wangzhen23
 * 2017/9/29.
 */
public class ExceptionInfo {
    private int startPc;
    private int endPc;
    private int handlerPc;
    private int catchType;

    public ExceptionInfo() {
    }

    public ExceptionInfo(int startPc, int endPc, int handlerPc, int catchType) {
        this.startPc = startPc;
        this.endPc = endPc;
        this.handlerPc = handlerPc;
        this.catchType = catchType;
    }

    public int getStartPc() {
        return startPc;
    }

    public void setStartPc(int startPc) {
        this.startPc = startPc;
    }

    public int getEndPc() {
        return endPc;
    }

    public void setEndPc(int endPc) {
        this.endPc = endPc;
    }

    public int getHandlerPc() {
        return handlerPc;
    }

    public void setHandlerPc(int handlerPc) {
        this.handlerPc = handlerPc;
    }

    public int getCatchType() {
        return catchType;
    }

    public void setCatchType(int catchType) {
        this.catchType = catchType;
    }
}
