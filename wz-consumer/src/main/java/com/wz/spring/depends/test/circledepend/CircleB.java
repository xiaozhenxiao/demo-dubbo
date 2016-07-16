package com.wz.spring.depends.test.circledepend;

/**
 * Created by Administrator on 2016/7/16.
 * Date:2016-07-16
 */
public class CircleB {
    private CircleA a;

    public CircleA getA() {
        return a;
    }

    public void setA(CircleA a) {
        this.a = a;
    }

    public CircleB(CircleA a)
    {
        this.a=a;
    }
    public CircleB()
    {

    }
}
