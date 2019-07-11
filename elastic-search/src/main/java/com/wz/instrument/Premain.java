package com.wz.instrument;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Premain
 * wangzhen23
 * 2019/7/10.
 */
public class Premain {
    public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
//        System.out.println("agentArgs:" + agentArgs);
        inst.addTransformer(new Transformer());
    }
}
