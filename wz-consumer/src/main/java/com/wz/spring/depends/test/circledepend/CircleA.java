package com.wz.spring.depends.test.circledepend;

/**
 * Created by Administrator on 2016/7/16.
 * Date:2016-07-16
 */
public class CircleA {
    private CircleB b;
    public CircleA(CircleB b)
    {
        this.b=b;
    }
    public CircleB getB() {
        return b;
    }
    public void setB(CircleB b) {
        this.b = b;
    }
    public CircleA()
    {

    }
}
