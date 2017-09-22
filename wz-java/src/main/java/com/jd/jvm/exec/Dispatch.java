package com.jd.jvm.exec;

/**
 * 单分派、多分派演示
 * wangzhen23
 * 2017/9/20.
 */
public class Dispatch {
    static class QQ {}

    static class _360{}

    public static class Father{
        public void hardChoice(QQ arg){
            System.out.println("fatcher choose qq");
        }

        public void hardChoice(_360 arg){
            System.out.println("father choose 360");
        }
    }

    public static class Son extends Father{
        public void hardChoice(QQ arg){
            System.out.println("Son choose qq");
        }

        public void hardChoice(_360 arg){
            System.out.println("son choose 360");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        father.hardChoice(new _360());
        son.hardChoice(new QQ());
    }
}
