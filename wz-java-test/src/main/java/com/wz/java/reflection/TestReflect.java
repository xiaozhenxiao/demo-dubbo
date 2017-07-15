package com.wz.java.reflection;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * java反射机制的测试
 * wangzhen23
 * 2017/7/15.
 */
public class TestReflect implements Serializable {
    public static void main(String[] args) throws Exception {
        TestReflect testReflect = new TestReflect();
        System.out.println(testReflect.getClass().getName());
        //com.wz.java.reflection.TestReflect

        Class<?> class1 = null;
        Class<?> class2 = null;
        Class<?> class3 = null;
        // 一般采用这种形式
        class1 = Class.forName("com.wz.java.reflection.TestReflect");
        class2 = new TestReflect().getClass();
        class3 = TestReflect.class;
        System.out.println("类名称   " + class1.getName());
        System.out.println("类名称   " + class2.getName());
        System.out.println("类名称   " + class3.getName());
        /*
            类名称   com.wz.java.reflection.TestReflect
            类名称   com.wz.java.reflection.TestReflect
            类名称   com.wz.java.reflection.TestReflect
        */


        Class clazz = Class.forName("com.wz.java.reflection.TestReflect");
        // 取得父类
        Class<?> parentClass = clazz.getSuperclass();
        System.out.println("clazz的父类为：" + parentClass.getName());
        // clazz的父类为： java.lang.Object
        // 获取所有的接口
        Class<?> intes[] = clazz.getInterfaces();
        System.out.println("clazz实现的接口有：");
        for (int i = 0; i < intes.length; i++) {
            System.out.println((i + 1) + "：" + intes[i].getName());
        }
        /*clazz实现的接口有：
        1：java.io.Serializable
                */

        class1 = Class.forName("com.wz.java.reflection.User");
        // 第一种方法，实例化默认构造方法，调用set赋值
        User user = (User) class1.newInstance();
        user.setAge(20);
        user.setName("Rollen");
        System.out.println("1:"+user);
        // 结果 User [age=20, name=Rollen]
        // 第二种方法 取得全部的构造函数 使用构造函数赋值
        Constructor<?> cons[] = class1.getConstructors();
        // 查看每个构造方法需要的参数
        for (int i = 0; i < cons.length; i++) {
            Class<?> clazzs[] = cons[i].getParameterTypes();
            System.out.print("cons[" + i + "] (");
            for (int j = 0; j < clazzs.length; j++) {
                if (j == clazzs.length - 1)
                    System.out.print(clazzs[j].getName());
                else
                    System.out.print(clazzs[j].getName() + ",");
            }
            System.out.println(")");
        }
        // 结果
        // cons[0] (int,java.lang.String)
        // cons[1] (java.lang.String)
        // cons[2] ()
        user = (User) cons[1].newInstance("xiaoxiao");
        System.out.println("2"+user);
        // 结果 User [age=0, name=Rollen]
        user = (User) cons[0].newInstance(20, "Rollen");
        System.out.println("3"+user);
        // 结果 User [age=20, name=Rollen]


        System.out.println("===============本类属性===============");
        // 取得本类的全部属性
        Field[] field = class1.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            // 权限修饰符
            int mo = field[i].getModifiers();
            String priv = Modifier.toString(mo);
            // 属性类型
            Class<?> type = field[i].getType();
            System.out.println(priv + " " + type.getName() + " " + field[i].getName() + ";");
        }

        System.out.println("==========实现的接口或者父类的属性=经测试结果并不对=========");
        // 取得实现的接口或者父类的属性
        Field[] filed1 = class1.getFields();
        for (int j = 0; j < filed1.length; j++) {
            // 权限修饰符
            int mo = filed1[j].getModifiers();
            String priv = Modifier.toString(mo);
            // 属性类型
            Class<?> type = filed1[j].getType();
            System.out.println(priv + " " + type.getName() + " " + filed1[j].getName() + ";");
        }

        System.out.println("+++++++++++++获取类的全部方法+++++++++++++");
        getMethods();

        System.out.println("============调用类的方法===============");
        reflectMethods();

        System.out.println("===============通过反射机制操作某个类的属性=======================");
        operateProperty();
    }

    private static void operateProperty() throws Exception {
        Class<?> clazz = Class.forName("com.wz.java.reflection.User");
        Object obj = clazz.newInstance();
        // 可以直接对 private 的属性赋值
        Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);
        field.set(obj, "Java反射机制");
        System.out.println(field.get(obj));
    }

    public static void getMethods() throws Exception {
        Class<?> clazz = Class.forName("com.wz.java.reflection.User");
        Method method[] = clazz.getMethods();
        for (int i = 0; i < method.length; ++i) {
            Class<?> returnType = method[i].getReturnType();
            Class<?> para[] = method[i].getParameterTypes();
            int temp = method[i].getModifiers();
            System.out.print(Modifier.toString(temp) + " ");
            System.out.print(returnType.getName() + "  ");
            System.out.print(method[i].getName() + " ");
            System.out.print("(");
            for (int j = 0; j < para.length; ++j) {
                System.out.print(para[j].getName() + " " + "arg" + j);
                if (j < para.length - 1) {
                    System.out.print(",");
                }
            }
            Class<?> exce[] = method[i].getExceptionTypes();
            if (exce.length > 0) {
                System.out.print(") throws ");
                for (int k = 0; k < exce.length; ++k) {
                    System.out.print(exce[k].getName() + " ");
                    if (k < exce.length - 1) {
                        System.out.print(",");
                    }
                }
            } else {
                System.out.print(")");
            }
            System.out.println();
        }
    }

    public static void reflectMethods() throws Exception {
        User user = new User();
        user.setAge(20);
        user.setName("Rollen");
        Class<?> clazz = User.class;
        // 调用TestReflect类中的reflect1方法
        Method method = clazz.getMethod("getName");
        System.out.println("result="+method.invoke(user));
    }

}
