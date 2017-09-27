package com.jd.romote.exec.bytecode;

import com.jd.romote.exec.ByteUtils;

import java.io.IOException;

/**
 * TODO
 * wangzhen23
 * 2017/9/22.
 */
public class MainClass {
    public static void main(String[] args) throws IOException {
        String classPath = "E:/myworkspaces/idea-dubbo/wz-java/target/classes/com/jd/romote/exec/bytecode/Person.class";
        BytecodeAnalysis.bindClass(classPath);
        BytecodeAnalysis.analysis();
    }

    public static void printByte(byte[] bytes) {
        for (byte aByte : bytes) {
            System.out.printf(aByte + "\t");
        }
    }
}
