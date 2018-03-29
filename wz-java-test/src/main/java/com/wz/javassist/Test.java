package com.wz.javassist;

import javassist.*;

/**
 * TODO
 * wangzhen23
 * 2018/3/27.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.wz.javassist.Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore(" System.out.println(\"Hello.say():\");");

        CtMethod mm = CtNewMethod.make("public String sayHello(String dy) { $proceed(0, dy);return dy;}", cc, "this", "say");
        cc.addMethod(mm);

        CtMethod m3 = new CtMethod(CtClass.intType, "move", new CtClass[] { CtClass.intType }, cc);
        cc.addMethod(m3);
        m3.setBody("{ x += $1;return 1;}");
        cc.setModifiers(cc.getModifiers() & ~Modifier.ABSTRACT);

        Class c = cc.toClass();
        Hello h = (Hello)c.newInstance();
        h.say(123, "world");
        cc.writeFile("E:/myworkspaces/idea-dubbo/wz-java-test/src/main/java");
    }

}
