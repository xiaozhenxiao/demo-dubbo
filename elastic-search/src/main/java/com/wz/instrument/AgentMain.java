package com.wz.instrument;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * TODO
 * wangzhen23
 * 2019/7/10.
 */
public class AgentMain {
    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException, InterruptedException {
        inst.addTransformer(new Transformer(), true);
        inst.retransformClasses(TransClass.class);
        System.out.println("Agent Main Done");
    }
}
