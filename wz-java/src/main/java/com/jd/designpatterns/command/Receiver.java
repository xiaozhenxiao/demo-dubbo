package com.jd.designpatterns.command;

/**
 * TODO
 * wangzhen23
 * 2017/10/31.
 */
public abstract class Receiver {
    //抽象接收者，定义每个接收者都必须完成的业务
    public abstract void doSomething();
}
