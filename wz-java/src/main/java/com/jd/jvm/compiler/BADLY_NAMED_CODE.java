package com.jd.jvm.compiler;

/**
 * 反面教材
 * javac -processor com.jd.jvm.compiler.NameCheckProcessor -encoding TUF-8 com\jd\jvm\compiler\BADLY_NAMED_CODE.java
 * wangzhen23
 * 2017/9/25.
 */
public class BADLY_NAMED_CODE {

    enum colors {
        red, blue, green;
    }

    static final int _FORTY_TWO = 42;

    public static int NOT_A_CONSTANT = _FORTY_TWO;

    protected void BADLY_NAMED_CODE() {
        return;
    }

    public void NOTcamelCASEmethodNAME() {
        return;
    }
}
