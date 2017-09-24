package com.jd.romote.exec.bytecode;

import com.jd.romote.exec.ByteUtils;
import com.jd.romote.exec.bytecode.constant.ContantIndex;
import com.jd.romote.exec.bytecode.constant.ContantItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private static int u8 = 8;

    /**
     * 常量池中的常量,未解析符号引用
     */
    private static Map<Integer, ContantIndex> constantIndexMap;
    /**
     * 常量池中的常量，符号引用已解析为字符串
     */
    private static Map<Integer, String> constantMap;

    public static void bindClass(String classPathName) throws IOException {
        try (FileInputStream is = new FileInputStream(classPathName)) {
            classByte = new byte[is.available()];
            int length = is.read(classByte);
            System.out.println("#################################################");
            System.out.println("class size:" + length);
            System.out.println("#################################################");
        }
    }

    public static String analysis() {
        int offset = 0;
        /* 读取魔数 */
        int magic = ByteUtils.bytes2Int(classByte, offset, u4);
        System.out.println("magic: " + String.valueOf(Integer.toHexString(magic).toUpperCase()));

        offset += u4;
        /* 版本号 */
        int minorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        int majorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("version:" + (minorVersion == 0 ? "" : minorVersion + ".") + majorVersion);
        offset += u2;
        /* 解析常量池 */
        offset = analysisConstantPool(offset);
        analysisSymbolReference();

        /* 解析访问标志 */
        int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.printf("access_flags:0x%04x\n", accessFlags);
        offset += u2;

        /* 解析类索引，父类索引，接口索引 */
        offset = analysisClassIndex(offset);

        /* 解析字段表集合 */
        offset = analysisFiledTables(offset);

        /* 解析方法表集合 */
        offset = analysisMethodTables(offset);
        return null;
    }

    private static void analysisSymbolReference() {
        for (Integer index : constantIndexMap.keySet()) {
            ContantIndex contantIndex = constantIndexMap.get(index);
            if (contantIndex.getTag() < 7) {
                constantMap.put(index, contantIndex.getValue().replace("/", "."));
                continue;
            }
            if (contantIndex.getTag() < 9) {
                constantMap.put(index, constantIndexMap.get(Integer.valueOf(contantIndex.getValue())).getValue().replace("/", "."));
                continue;
            }
            if (contantIndex.getTag() < 12) {
                String[] indexs = contantIndex.getValue().split(":");
                int classIndex = Integer.valueOf(indexs[0]);
                int descriptorIndex = Integer.valueOf(indexs[1]);
                String className = constantIndexMap.get(Integer.valueOf(constantIndexMap.get(classIndex).getValue())).getValue();
                ContantIndex nameAndTypeIndex = constantIndexMap.get(descriptorIndex);
                String[] nameAndType = nameAndTypeIndex.getValue().split(":");
                int nameIndex = Integer.valueOf(nameAndType[0]);
                int typeIndex = Integer.valueOf(nameAndType[1]);
                String name = constantIndexMap.get(nameIndex).getValue();
                String descriptor = constantIndexMap.get(typeIndex).getValue().replace("/", ".");
                int ix = descriptor.indexOf(')') + 1;
                String allName = descriptor.substring(ix) + " " + className.replaceAll("/", ".") + "." + name + descriptor.substring(0, ix);
                constantMap.put(index, allName);
            }
            if (contantIndex.getTag() == 12) {
                ContantIndex nameAndTypeIndex = constantIndexMap.get(index);
                String[] nameAndType = nameAndTypeIndex.getValue().split(":");
                int nameIndex = Integer.valueOf(nameAndType[0]);
                int typeIndex = Integer.valueOf(nameAndType[1]);
                String name = constantIndexMap.get(nameIndex).getValue();
                String descriptor = constantIndexMap.get(typeIndex).getValue().replace("/", ".");
                int ix = descriptor.indexOf(')') + 1;
                String allName = descriptor.substring(ix) + " " + name + descriptor.substring(0, ix);
                constantMap.put(index, allName);
            }
        }
    }

    private static int analysisMethodTables(int offset) {
        System.out.println("========================方法表START=====================");
        int methodCount = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("method_count:" + methodCount);
        offset += u2;
        for (int i = 0; i < methodCount; i++) {
            System.out.println("-------------------------------方法" + i + "-------------------------------");
            int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.printf("access_flags:0x%04x\n", accessFlags);
            int nameIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
            System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
            int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u4, u2);
            System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));

            offset += u2 + u4;
            int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.println("attributes_count:" + attributesCount);
            offset += u2;
            for (int j = 0; j < attributesCount; j++) {
                /* 解析属性 */
                offset = analysisAttribute(offset);
            }
        }
        System.out.println("========================方法表END=====================");
        return offset;
    }

    private static int analysisFiledTables(int offset) {
        int fieldsCount = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("=====================字段表START======================");
        System.out.println("fields_count:" + fieldsCount);
        offset += u2;
        for (int i = 0; i < fieldsCount; i++) {
            System.out.println("--------------------字段" + i + "----------------------");
            int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.printf("access_flags:0x%04x\n", accessFlags);
            int nameIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
            System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
            int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u4, u2);
            System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));

            offset += u2 + u4;
            int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.println("attributes_count:" + attributesCount);
            offset += u2;
            for (int j = 0; j < attributesCount; j++) {
                /* 解析属性 */
                offset = analysisAttribute(offset);
            }
        }
        System.out.println("=====================字段表END======================");
        return offset;
    }

    private static int analysisCodeAttr(int offset) {
        int maxStack = ByteUtils.bytes2Int(classByte, offset, u2);
        int maxLocals = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        offset += u4;
        System.out.println("max_stack:" + maxStack);
        System.out.println("max_locals:" + maxLocals);

        int codeLength = ByteUtils.bytes2Int(classByte, offset, u4);
        System.out.println("code_length:" + codeLength);
        // TODO: 2017/9/24 解析字节码
        offset += u4 + codeLength;
        int exceptionTableLength = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        System.out.println("exception_table_length:" + exceptionTableLength);//exception_table_length代表exception_table[]的大小
        for (int i = 0; i < exceptionTableLength; i++) {
            int startPc = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            int endPc = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            int handlerPc = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            int catchTypeIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            System.out.println("start_pc:" + startPc);
            System.out.println("end_pc:" + endPc);
            System.out.println("handler_pc:" + handlerPc);
            System.out.println("catch_type:#" + catchTypeIndex);
        }
        int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("attributes_count:" + attributesCount);
        offset += u2;
        for (int i = 0; i < attributesCount; i++) {
            offset = analysisAttribute(offset);
        }
        return offset;
    }

    private static int analysisAttribute(int offset) {
        int attributeNameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("attribute_name_index:#" + attributeNameIndex + "->" + constantMap.get(attributeNameIndex));
        int attributeLength = ByteUtils.bytes2Int(classByte, offset + u2, u4);
        offset += u4 + u2;
        System.out.println("attribute_length:" + attributeLength);

        if (constantMap.get(attributeNameIndex).equals("Code")) {//解析Code属性
            System.out.println("====================Code START=======================");
            offset = analysisCodeAttr(offset);
            System.out.println("====================Code END=======================");
        } else if (constantMap.get(attributeNameIndex).equals("LineNumberTable")) {
            int lineNumberTableLength = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^LineNumberTable START^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println("line_number_table_length:" + lineNumberTableLength);
            System.out.println("start_pc  <--->  line_number");
            for (int i = 0; i < lineNumberTableLength; i++) {
                int startPc = ByteUtils.bytes2Int(classByte, offset, u2);
                int lineNumber = ByteUtils.bytes2Int(classByte, offset + u2, u2);
                System.out.println(startPc + "  <--->  " + lineNumber);
                offset += u4;
            }
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^LineNumberTable END^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        } else if (constantMap.get(attributeNameIndex).equals("LocalVariableTable")) {
            int localVariableTableLength = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!LocalVariableTable START!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
            System.out.println("local_variable_table_length:" + localVariableTableLength);
            offset += u2;
            for (int i = 0; i < localVariableTableLength; i++) {
                int startPc = ByteUtils.bytes2Int(classByte, offset, u2);
                offset += u2;
                int length = ByteUtils.bytes2Int(classByte, offset, u2);
                offset += u2;
                int nameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
                offset += u2;
                int descriptorIndex = ByteUtils.bytes2Int(classByte, offset, u2);
                offset += u2;
                int index = ByteUtils.bytes2Int(classByte, offset, u2);
                offset += u2;
                System.out.println("----------Variable" + i + "-----------");
                System.out.println("start_pc:" + startPc);
                System.out.println("length:" + length);
                System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
                System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));
                System.out.println("index:" + index);
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!LocalVariableTable END!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        } else if (constantMap.get(attributeNameIndex).equals("ConstantValue")) {
            int constantvalueIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            offset += u2;
            System.out.println("constantvalue_index:#" + constantvalueIndex + " -> " + constantMap.get(constantvalueIndex));
        } else {
            // TODO: 2017/9/23 属性解析
            offset += attributeLength;
        }
        return offset;
    }

    private static int analysisClassIndex(int offset) {
        int thisClassIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int superClassIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("this_class_index:#" + thisClassIndex + "->" + constantMap.get(thisClassIndex));
        System.out.println("super_class_index:#" + superClassIndex + "->" + constantMap.get(superClassIndex));
        offset += u4;

        int interfacesCount = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("interfaces_count:" + interfacesCount);

        offset += u2;
        for (int i = 0; i < interfacesCount; i++) {
            int interfaceIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.println("interface_index:#" + interfaceIndex + "->" + constantMap.get(interfaceIndex));
            offset += u2;
        }
        return offset;
    }

    private static int analysisConstantPool(int offset) {
        /* 常量池大小 */
        int cpc = ByteUtils.bytes2Int(classByte, offset, u2);
        constantMap = new HashMap<>(cpc);
        constantIndexMap = new HashMap<>(cpc);
        System.out.println("constant_pool_count:" + cpc);
        System.out.println("============================常量池start===================================");
        offset += u2;
        for (int i = 1; i < cpc; i++) {
            int tag = ByteUtils.bytes2Int(classByte, offset, u1);
            System.out.println("-------------------------------" + i + "---------------------------------");
            System.out.println("tag:" + tag);
            offset += u1;
            switch (tag) {
                case ContantItem.UTF8_TAG:
                    offset = analysisConstantUtf8(offset, i);
                    break;
                case ContantItem.INTEGER_TAG:
                    offset = analysisConstantInteger(offset, i);
                    break;
                case ContantItem.FLOAT_TAG:
                    offset = analysisConstantFloat(offset, i);
                    break;
                case ContantItem.LONG_TAG:
                    offset = analysisConstantLong(offset, i);
                    ++i;
                    break;
                case ContantItem.DOUBLE_TAG:
                    offset = analysisConstantDouble(offset, i);
                    ++i;
                    break;
                case ContantItem.CLASS_TAG:
                    offset = analysisConstantClass(offset, i);
                    break;
                case ContantItem.STRING_TAG:
                    offset = analysisConstantString(offset, i);
                    break;
                case ContantItem.FIELDREF_TAG:
                    offset = analysisConstantFieldref(offset, i);
                    break;
                case ContantItem.METHODREF_TAG:
                    offset = analysisConstantMethodRef(offset, i);
                    break;
                case ContantItem.INTERFACEMETHODREF_TAG:
                    offset = analysisConstantInterfaceMethodRef(offset, i);
                    break;
                case ContantItem.NAMEANDTYPE_TAG:
                    offset = analysisConstantNameAndType(offset, i);
                    break;
                case ContantItem.METHODHANDLE_TAG:
                    offset = analysisConstantMethodHandle(offset, i);
                    break;
                case ContantItem.METHODTYPE_TAG:
                    offset = analysisConstantMethodType(offset, i);
                    break;
                case ContantItem.INVOKEDYNAMIC_TAG:
                    offset = analysisConstantInvokeDynamic(offset, i);
                    break;
                default:
                    System.out.println(tag + "**************************" + tag);
            }
        }
        System.out.println("==============================常量池end=================================");
        return offset;
    }

    private static int analysisConstantInvokeDynamic(Integer offset, int index) {
        int bootstarpMethodAttrIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int nameAndTypeIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("InvokeDynamic_bootstarp_method_attr_index:#" + bootstarpMethodAttrIndex);
        System.out.println("InvokeDynamic_name_and_type_index:#" + nameAndTypeIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.INVOKEDYNAMIC_TAG, String.valueOf(bootstarpMethodAttrIndex) + ":" + String.valueOf(nameAndTypeIndex)));
        return offset + u4;
    }

    private static int analysisConstantMethodType(Integer offset, int index) {
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("methodType_descriptor_index:#" + descriptorIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODTYPE_TAG, String.valueOf(descriptorIndex)));
        return offset + u2;
    }

    private static int analysisConstantMethodHandle(Integer offset, int index) {
        int referenceKind = ByteUtils.bytes2Int(classByte, offset, u1);
        int referenceIndex = ByteUtils.bytes2Int(classByte, offset + u1, u2);
        System.out.println("methodHandle_reference_kind:" + referenceKind);
        System.out.println("methodHandle_reference_index:#" + referenceIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODHANDLE_TAG, String.valueOf(referenceKind) + ":" + String.valueOf(referenceIndex)));
        return offset + u1 + u2;
    }

    private static int analysisConstantNameAndType(Integer offset, int index) {
        int nameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int typeIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("nameAndType_name_index:#" + nameIndex);
        System.out.println("nameAndType_type_index:#" + typeIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.NAMEANDTYPE_TAG, String.valueOf(nameIndex) + ":" + String.valueOf(typeIndex)));
        return offset + u4;
    }

    private static int analysisConstantInterfaceMethodRef(Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("interfaceMethod_class_index:#" + classIndex);
        System.out.println("interfaceMethod_descriptor_index:#" + descriptorIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.INTERFACEMETHODREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantMethodRef(Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("method_class_index:#" + classIndex);
        System.out.println("method_descriptor_index:#" + descriptorIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantFieldref(Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("field_class_index:#" + classIndex);
        System.out.println("field_descriptor_index:#" + descriptorIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.FIELDREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantString(Integer offset, int index) {
        int stringIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("string_index:#" + stringIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.STRING_TAG, String.valueOf(stringIndex)));
        return offset + u2;
    }

    private static int analysisConstantClass(Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("class_index:#" + classIndex);

        constantIndexMap.put(index, new ContantIndex(false, ContantItem.CLASS_TAG, String.valueOf(classIndex)));
        return offset + u2;
    }

    private static int analysisConstantDouble(Integer offset, int index) {
        double doubleValue = ByteUtils.bytes2Double1(classByte, offset);
        System.out.println("double_value:" + doubleValue);

        constantIndexMap.put(index, new ContantIndex(ContantItem.DOUBLE_TAG, String.valueOf(doubleValue)));
        return offset + u8;
    }

    private static int analysisConstantLong(Integer offset, int index) {
        long longValue = ByteUtils.bytes2Long1(classByte, offset);
        System.out.println("long_value:" + longValue);

        constantIndexMap.put(index, new ContantIndex(ContantItem.LONG_TAG, String.valueOf(longValue)));
        return offset + u8;
    }

    private static int analysisConstantFloat(Integer offset, int index) {
        float floatValue = ByteUtils.bytes2Float1(classByte, offset);
        System.out.println("float_value:" + floatValue);

        constantIndexMap.put(index, new ContantIndex(ContantItem.FLOAT_TAG, String.valueOf(floatValue)));
        return offset + u4;
    }

    private static int analysisConstantInteger(Integer offset, int index) {
        int intValue = ByteUtils.bytes2Int(classByte, offset, u4);
        System.out.println("integer_value:" + intValue);

        constantIndexMap.put(index, new ContantIndex(ContantItem.INTEGER_TAG, String.valueOf(intValue)));
        return offset + u4;
    }

    private static int analysisConstantUtf8(Integer offset, int index) {
        int length = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        System.out.println("constant_length:" + length);
        String constantValue = ByteUtils.bytes2String(classByte, offset, length);
        System.out.println("utf8_value:" + constantValue);

        constantIndexMap.put(index, new ContantIndex(ContantItem.UTF8_TAG, constantValue));
        return offset + length;
    }
}
