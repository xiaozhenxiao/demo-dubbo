package com.wz.jdk.java.thread.future;

/**
 * FutureData是Future模式的关键，它实际上是真实数据RealData的代理，封装了获取RealData的等待过程。
 * wangzhen23
 * 2017/6/14.
 */
public class FutureData implements Data{
    private RealData realData = null; //FutureData是RealData的封装
    private boolean isReady = false;  //是否已经准备好

    public synchronized void setRealData(RealData realData) {
        if(isReady)
            return;
        this.realData = realData;
        isReady = true;
        notifyAll(); //RealData已经被注入到FutureData中了，通知getResult()方法
    }

    @Override
    public synchronized String getResult() throws InterruptedException {
        if(!isReady) {
            wait(); //一直等到RealData注入到FutureData中
        }
        return realData.getResult();
    }
}
