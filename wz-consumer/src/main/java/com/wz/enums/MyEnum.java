package com.wz.enums;

/**
 * 枚举用法
 * wangzhen23
 * 2019/1/18.
 */
public enum MyEnum {
    T1{
        String getName() {
            return "t1";
        }

        void console(String log) {
            System.out.println("tl:" + log);
        }
    },
    T2{
        String getName() {
            return "t2";
        }

        void console(String log) {
            System.out.println("t2:" + log);
        }
    }
    ;

    String getName(){return "";}

    void console(String log){
        throw new RuntimeException("no implement");
    }
}
