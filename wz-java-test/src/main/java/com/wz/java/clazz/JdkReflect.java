package com.wz.java.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by wangzhen on 2016-07-14.
 */
public class JdkReflect {
    public static void main(String[] args) {
        try {
            Class childClassType = Class.forName("com.wz.java.clazz.ChildClass");
            Class parentClassType = Class.forName("com.wz.java.clazz.ParentClass");
            Constructor<ParentClass> constructor = parentClassType.getDeclaredConstructor((Class[]) null);
            ParentClass parent = constructor.newInstance();
            parent.doSomething();
            System.out.println(parent.doParent());

            Constructor<ChildClass> constructor1 = childClassType.getDeclaredConstructor((Class[]) null);
            ChildClass child = constructor1.newInstance();
            child.doSomething();
            System.out.println(child.doChild());
//            classType.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

class ParentClass{
    public String name = "爸爸";

    public void doSomething(){
        System.out.println("父亲的行为");
    }
    public String doParent(){
        return "父亲做自己的事情了";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ChildClass extends ParentClass{
    public String name = "儿子";

    @Override
    public void doSomething() {
        System.out.println("儿子的行为");
        super.doSomething();
    }

    public String doChild(){
        return "孩子做孩子该做的事情";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
