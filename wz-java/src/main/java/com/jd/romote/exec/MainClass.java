package com.jd.romote.exec;

import com.jd.romote.exec.bytecode.BytecodeAnalysis;
import com.jd.romote.exec.bytecode.java.ClassFile;

import java.io.IOException;

/**
 * wangzhen23
 * 2017/9/22.
 */
public class MainClass {
    public static void main(String[] args) throws IOException {
        String classPath = "E:/myworkspaces/idea-dubbo/wz-java/target/classes/com/jd/romote/exec/bytecode/Person.class";
        BytecodeAnalysis.bindClass(classPath);
        ClassFile classFile = BytecodeAnalysis.analysis();
        System.out.println(classFile);
    }

    public static void printByte(byte[] bytes) {
        for (byte aByte : bytes) {
            System.out.printf(aByte + "\t");
        }
    }

    public static void testByteUtils(){
        byte[] result = ByteUtils.float2Bytes(88.899f, 4);
        System.out.println("float to byte:");
        printByte(result);

        float floatValue = ByteUtils.bytes2Float(result, 0);
        System.out.println("= " + floatValue);

        byte[] doubleBytes = ByteUtils.double2Bytes(668.888, 8);
        System.out.println("double to byte:");
        printByte(doubleBytes);

        Double dou = ByteUtils.bytes2Double(doubleBytes, 0);
        System.out.println("= " + dou);

        byte[] longBytes = ByteUtils.long2Bytes(668888, 8);
        System.out.println("long to byte:");
        printByte(longBytes);

        long lon = ByteUtils.bytes2Long(longBytes, 0);
        System.out.println("= " + lon);
    }
}
