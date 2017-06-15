package com.wz.jdk.java.thread.future;

/**
 * RealData是最终需要使用的数据，它的构造函数很慢
 * wangzhen23
 * 2017/6/14.
 */
public class RealData implements Data{
    protected String data;

    public RealData(String data) {
        //利用sleep方法来表示RealData构造过程是非常缓慢的
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.data = "==================" + data;
    }

    @Override
    public String getResult() {
        return data;
    }
}
