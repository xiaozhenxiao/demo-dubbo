package com.jd.jvm.exec;

/**
 * 方法动态分派演示
 * wangzhen23
 * 2017/9/20.
 */
public class DynamicDispatch {
    static abstract class Human{
        public String name = "Human";
        protected abstract void sayHello();
    }

    static class Man extends Human{
        public String name = "Man";
        @Override
        protected void sayHello() {
            System.out.println("man say hello");
        }
    }

    static class Woman extends Human{
        public String name = "Woman";
        @Override
        protected void sayHello() {
            System.out.println("woman say hello");
        }
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        man.sayHello();
        System.out.println("man1:" + man.name);
        woman.sayHello();
        System.out.println("woman2:" + woman.name);

        man = new Woman();
        man.sayHello();
        System.out.println("man2:" + man.name);
        System.out.println("man3:" + ((Woman)man).name);
    }
}
