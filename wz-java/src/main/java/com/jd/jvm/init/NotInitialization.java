package com.jd.jvm.init;

/**
 * 例一：对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过其子类来引用父类中定义的静态字段，只会触发父类的初始化而不会触发子类的初始化。
 * 例二：触发[Lcom.jd.jvm.init.SuperClass类的实例化，这个类代表了元素类型为com.jd.jvm.init.SuperClass的一维数组。
 * 例三：在编译阶段通过常量传播优化，已经将此常量的值“hello world”存储到了NotInitialization类的常量池中，所以NotInitialization对常量ConstClass.HELLOWORLD的引用
 * 实际都被转化为NotInitialization类对自身常量池的引用。
 * wangzhen23
 * 2017/9/18.
 */
public class NotInitialization {
    public static void main(String[] args) {
        /** 被动引用例一 **/
//        System.out.println(SubClass.value);

        /** 被动引用例二 **/
        SuperClass[] sca = new SuperClass[10];
        System.out.println(sca.length);

        /** 被动引用例三 **/
        System.out.println(ConstClass.HELLOWORLD);
    }
}
