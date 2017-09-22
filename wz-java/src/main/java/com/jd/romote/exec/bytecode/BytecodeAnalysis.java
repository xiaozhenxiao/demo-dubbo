package com.jd.romote.exec.bytecode;

import com.jd.romote.exec.ByteUtils;
import com.jd.romote.exec.bytecode.constant.ContantItem;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 字节码解析成可视化
 * wangzhen23
 * 2017/9/22.
 */
public class BytecodeAnalysis {
    //Class文件的二进制数组
    private static byte[] classByte;
    private static int u1 = 1;
    private static int u2 = 2;
    private static int u4 = 4;

    /**
     * 常量池中11种常量所占的长度，CONSTANT_Utf8_info型常量除外，因为它不是定长的
     */
    private static final int[] CONSTANT_ITEM_LENGTH = { -1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5 };

    public static void bindClass(String classPathName) throws IOException {
        FileInputStream is = new FileInputStream(classPathName);
        classByte = new byte[is.available()];
        is.read(classByte);
        is.close();
    }

    public static String analysis(){
        int offset = 0;
        /** 读取魔数 **/
        int magic = ByteUtils.bytes2Int(classByte, offset, u4);
        System.out.println("magic: " + String.valueOf(Integer.toHexString(magic).toUpperCase()));

        offset += u4;
        /** 版本号 **/
        int minorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        int majorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("version:" + (minorVersion==0? "" : minorVersion + ".") + majorVersion);
        offset += u2;
        /** 解析常量池 **/
        analysisConstantPool(offset);

        return null;
    }

    private static void analysisConstantPool(int offset) {
        /** 常量池大小 **/
        int cpc = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("constant_pool_count:" + cpc);
        offset += u2;
        for (int i = 1; i < cpc; i++) {
            int tag = ByteUtils.bytes2Int(classByte, offset, u1);
            offset += u1;
            switch (tag){
                case ContantItem.UTF8_TAG:
                    analysisConstantUtf8(offset);
                    break;
                case ContantItem.INTEGER_TAG:
                    analysisConstantInteger(offset);
                    break;
                case ContantItem.FLOAT_TAG:
                    analysisConstantFloat(offset);
                    break;
                case ContantItem.LONG_TAG:
                    analysisConstantLong(offset);
                    break;
                case ContantItem.DOUBLE_TAG:
                    analysisConstantDouble(offset);
                    break;
                case ContantItem.CLASS_TAG:
                    analysisConstantClass(offset);
                    break;
                case ContantItem.STRING_TAG:
                    analysisConstantString(offset);
                    break;
                case ContantItem.FIELDREF_TAG:
                    analysisConstantFieldref(offset);
                    break;
                case ContantItem.METHODREF_TAG:
                    analysisConstantMethodRef(offset);
                    break;
                case ContantItem.INTERFACEMETHODREF_TAG:
                    analysisConstantInterfaceMethodRef(offset);
                    break;
                case ContantItem.NAMEANDTYPE_TAG:
                    analysisConstantNameAndType(offset);
                    break;
                case ContantItem.METHODHANDLE_TAG:
                    analysisConstantMethodHandle(offset);
                    break;
                case ContantItem.METHODTYPE_TAG:
                    analysisConstantMethodType(offset);
                    break;
                case ContantItem.INVOKEDYNAMIC_TAG:
                    analysisConstantInvokeDynamic(offset);
                    break;
                default:
                    System.out.println(tag+"**************************"+tag);
            }
        }


    }

    private static void analysisConstantInvokeDynamic(Integer offset) {

    }

    private static void analysisConstantMethodType(Integer offset) {

    }

    private static void analysisConstantMethodHandle(Integer offset) {

    }

    private static void analysisConstantNameAndType(Integer offset) {

    }

    private static void analysisConstantInterfaceMethodRef(Integer offset) {

    }

    private static void analysisConstantMethodRef(Integer offset) {

    }

    private static void analysisConstantFieldref(Integer offset) {

    }

    private static void analysisConstantString(Integer offset) {

    }

    private static void analysisConstantClass(Integer offset) {

    }

    private static void analysisConstantDouble(Integer offset) {

    }

    private static void analysisConstantLong(Integer offset) {

    }

    private static void analysisConstantFloat(Integer offset) {

    }

    private static void analysisConstantInteger(Integer offset) {

    }

    private static void analysisConstantUtf8(Integer offset) {

    }
}
