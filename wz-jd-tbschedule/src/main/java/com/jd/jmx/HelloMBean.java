package com.jd.jmx;

/**
 * MBean接口
 * wangzhen23
 * 2017/8/22.
 */
public interface HelloMBean {
    public String getName();
    public void setName(String name);
    public void printHello();
    public void printHello(String whoName);
}
