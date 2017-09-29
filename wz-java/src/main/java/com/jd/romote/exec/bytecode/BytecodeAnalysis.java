package com.jd.romote.exec.bytecode;

import com.jd.romote.exec.ByteUtils;
import com.jd.romote.exec.bytecode.constant.ContantIndex;
import com.jd.romote.exec.bytecode.constant.ContantItem;
import com.jd.romote.exec.bytecode.java.ClassFile;
import com.jd.romote.exec.bytecode.java.FieldInfo;
import com.jd.romote.exec.bytecode.java.MethodInfo;
import com.jd.romote.exec.bytecode.java.attribute.*;
import com.jd.romote.exec.bytecode.java.constant.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 字节码解析成可视化
 * wangzhen23
 * 2017/9/22.
 */
public class BytecodeAnalysis {
    private static Map<Integer, String> instructions = new HashMap<>(256);
    private static ArrayList<Integer> instructions_1 = new ArrayList<>();
    private static ArrayList<Integer> instructions_2 = new ArrayList<>();
    //Class文件的二进制数组
    private static byte[] classByte;
    private static int u1 = 1;
    private static int u2 = 2;
    private static int u4 = 4;
    private static int u8 = 8;

    private static String[] addVariables;
    private static boolean localFlag = false;
    private static ArrayList<Integer> addCode;

    static {
        InputStream is = BytecodeAnalysis.class.getResourceAsStream("/bytecode.properties");
        try {
            Properties properties = new Properties();
            properties.load(is);
            properties.entrySet();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Integer code = Integer.parseInt(entry.getKey().toString(), 16);
                String name = (String) entry.getValue();
                instructions.put(code, name);
            }
            //添加有操作数的指令
            instructions_1.add(Integer.parseInt("15", 16));
            instructions_1.add(Integer.parseInt("36", 16));
            instructions_1.add(Integer.parseInt("12", 16));
            instructions_1.add(Integer.parseInt("10", 16));
            instructions_2.add(Integer.parseInt("b7", 16));
            instructions_2.add(Integer.parseInt("b5", 16));
            instructions_2.add(Integer.parseInt("14", 16));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    public static ClassFile analysis() {
        ClassFile classFile = new ClassFile();
        int offset = 0;
        /* 读取魔数 */
        int magic = ByteUtils.bytes2Int(classByte, offset, u4);
        classFile.setMagic(String.valueOf(Integer.toHexString(magic).toUpperCase()));
        System.out.println("magic: " + String.valueOf(Integer.toHexString(magic).toUpperCase()));

        offset += u4;
        /* 版本号 */
        int minorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        int majorVersion = ByteUtils.bytes2Int(classByte, offset, u2);
        classFile.setMinorVersion(minorVersion);
        classFile.setMajorVersion(majorVersion);
        System.out.println("version:" + (minorVersion == 0 ? "" : minorVersion + ".") + majorVersion);
        offset += u2;
        /* 解析常量池 */
        offset = analysisConstantPool(offset, classFile);
        analysisSymbolReference();

        /* 解析访问标志 */
        int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
        classFile.setAccessFlags(accessFlags);
        System.out.printf("access_flags:0x%04x\n", accessFlags);
        offset += u2;

        /* 解析类索引，父类索引，接口索引 */
        offset = analysisClassIndex(offset, classFile);

        /* 解析字段表集合 */
        offset = analysisFiledTables(offset, classFile);

        /* 解析方法表集合 */
        offset = analysisMethodTables(offset, classFile);
        decompileByteCode();
        System.out.println("offset:" + offset);
        return classFile;
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

    private static int analysisMethodTables(int offset, ClassFile classFile) {
        System.out.println("========================方法表START=====================");
        int methodCount = ByteUtils.bytes2Int(classByte, offset, u2);
        classFile.setMethodCount(methodCount);
        ArrayList<MethodInfo> methods = new ArrayList<>(methodCount);
        classFile.setMethods(methods);
        System.out.println("method_count:" + methodCount);
        offset += u2;
        for (int i = 0; i < methodCount; i++) {
            System.out.println("-------------------------------方法" + i + "-------------------------------");
            MethodInfo methodInfo = new MethodInfo();
            methods.add(methodInfo);
            int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
            System.out.printf("access_flags:0x%04x\n", accessFlags);
            methodInfo.setAccessFlags(accessFlags);
            int nameIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
            methodInfo.setNameIndex(nameIndex);
            System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
            if(constantMap.get(nameIndex).equals("add")){
                localFlag = true;
            }else{
                localFlag = false;
            }
            int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u4, u2);
            System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));
            methodInfo.setDescriptorIndex(descriptorIndex);

            offset += u2 + u4;
            int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
            methodInfo.setAttributesCount(attributesCount);
            ArrayList<AttributeInfo> attributes = new ArrayList<>(attributesCount);
            methodInfo.setAttributes(attributes);
            System.out.println("attributes_count:" + attributesCount);
            offset += u2;
            for (int j = 0; j < attributesCount; j++) {
                /* 解析属性 */
                offset = analysisAttribute(offset, attributes);
            }
        }
        System.out.println("========================方法表END=====================");
        return offset;
    }

    private static int analysisFiledTables(int offset, ClassFile classFile) {
        int fieldsCount = ByteUtils.bytes2Int(classByte, offset, u2);
        classFile.setFieldsCount(fieldsCount);
        System.out.println("=====================字段表START======================");
        System.out.println("fields_count:" + fieldsCount);
        ArrayList<FieldInfo> fields = new ArrayList<>(fieldsCount);
        classFile.setFields(fields);
        offset += u2;
        for (int i = 0; i < fieldsCount; i++) {
            FieldInfo fieldInfo = new FieldInfo();
            fields.add(fieldInfo);
            System.out.println("--------------------字段" + i + "----------------------");
            int accessFlags = ByteUtils.bytes2Int(classByte, offset, u2);
            fieldInfo.setAccessFlags(accessFlags);
            System.out.printf("access_flags:0x%04x\n", accessFlags);
            int nameIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
            fieldInfo.setNameIndex(nameIndex);
            System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
            int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u4, u2);
            fieldInfo.setDescriptoeIndex(descriptorIndex);
            System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));

            offset += u2 + u4;
            int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
            fieldInfo.setAttributesCount(attributesCount);
            System.out.println("attributes_count:" + attributesCount);

            ArrayList<AttributeInfo> fieldAttributes = new ArrayList<>(attributesCount);
            fieldInfo.setAttributes(fieldAttributes);
            offset += u2;
            for (int j = 0; j < attributesCount; j++) {
                /* 解析属性 */
                offset = analysisAttribute(offset, fieldAttributes);
            }
        }
        System.out.println("=====================字段表END======================");
        return offset;
    }

    private static int analysisCodeAttr(int offset, CodeAttribute codeAttribute) {
        int maxStack = ByteUtils.bytes2Int(classByte, offset, u2);
        int maxLocals = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        codeAttribute.setMaxStack(maxStack);
        codeAttribute.setMaxLocals(maxLocals);
        offset += u4;
        System.out.println("max_stack:" + maxStack);
        System.out.println("max_locals:" + maxLocals);

        // TODO: 2017/9/24 解析字节码
        offset = analysisByteCode(offset, codeAttribute);

        int exceptionTableLength = ByteUtils.bytes2Int(classByte, offset, u2);
        codeAttribute.setExceptionTableLength(exceptionTableLength);
        ArrayList<ExceptionInfo> exceptionTables = new ArrayList<>();
        codeAttribute.setExceptionTable(exceptionTables);
        offset += u2;
        System.out.println("exception_table_length:" + exceptionTableLength);//exception_table_length代表exception_table[]的大小
        for (int i = 0; i < exceptionTableLength; i++) {
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            int startPc = ByteUtils.bytes2Int(classByte, offset, u2);
            exceptionInfo.setStartPc(startPc);
            offset += u2;
            int endPc = ByteUtils.bytes2Int(classByte, offset, u2);
            exceptionInfo.setEndPc(endPc);
            offset += u2;
            int handlerPc = ByteUtils.bytes2Int(classByte, offset, u2);
            exceptionInfo.setHandlerPc(handlerPc);
            offset += u2;
            int catchTypeIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            exceptionInfo.setCatchType(catchTypeIndex);
            offset += u2;
            System.out.println("start_pc:" + startPc);
            System.out.println("end_pc:" + endPc);
            System.out.println("handler_pc:" + handlerPc);
            System.out.println("catch_type:#" + catchTypeIndex);
        }
        int attributesCount = ByteUtils.bytes2Int(classByte, offset, u2);
        codeAttribute.setAttributesCount(attributesCount);
        ArrayList<AttributeInfo> attributes = new ArrayList<>(attributesCount);
        codeAttribute.setAttributes(attributes);
        System.out.println("attributes_count:" + attributesCount);
        offset += u2;
        for (int i = 0; i < attributesCount; i++) {
            offset = analysisAttribute(offset, attributes);
        }
        return offset;
    }

    private static int analysisByteCode(int offset, CodeAttribute codeAttribute) {
        int codeLength = ByteUtils.bytes2Int(classByte, offset, u4);
        codeAttribute.setCodeLength(codeLength);
        ArrayList<Integer> code = new ArrayList<>(codeLength);
        System.out.println("code_length:" + codeLength);
        System.out.println("=========指令START==========");
        int index = offset + u4;
        for (int i = 0; i < codeLength; i++) {
            int opcode = ByteUtils.bytes2Int(classByte, index, u1);
            code.add(opcode);
            index += u1;
            String name = instructions.get(opcode);
            if (Objects.nonNull(name)) {
                if (instructions_1.contains(opcode)) {
                    int operand = ByteUtils.bytes2Int(classByte, index, u1);
                    index += u1;
                    i++;
                    code.add(operand);
                    name += " " + operand;
                }else if (instructions_2.contains(opcode)) {
                    int operand1 = ByteUtils.bytes2Int(classByte, index, u1);
                    int operand2 = ByteUtils.bytes2Int(classByte, index + u1, u1);
                    index += u2;
                    i = i + 2;
                    code.add(operand1);
                    code.add(operand2);
                    name += " " + operand1 + " " + operand2;
                }
                System.out.println(name);
            }
        }
        codeAttribute.setCode(code);
        if(localFlag){
            addCode = code;
        }
        System.out.println("=========指令END==========");
        return offset + u4 + codeLength;
    }

    private static void decompileByteCode(){
        Stack<String> javaCodeStack = new Stack<>();
        for (int i = 0; i < addCode.size(); i++) {
            int opcode = addCode.get(i);
            if (Objects.nonNull(opcode)) {
                if (instructions_1.contains(opcode)) {
                    decompile(opcode, addCode.get(++i), javaCodeStack);
                }else if (instructions_2.contains(opcode)) {
                    decompile(opcode, addCode.get(++i), javaCodeStack);
                    decompile(opcode, addCode.get(++i), javaCodeStack);
                }else {
                    decompile(opcode, null, javaCodeStack);
                }
            }
        }
        System.out.println("==================================反编译========================================");
        for (String s : javaCodeStack) {
            System.out.println(s + ";");
        }
        System.out.println("==================================反编译========================================");
    }
    private static Stack<String> decompile(Integer opcode, Integer operand, Stack<String> javaCodeStack){
        if(opcode.equals(Integer.parseInt("1b", 16))){//iload_1
            javaCodeStack.push(addVariables[1]);
        }else if(opcode.equals(Integer.parseInt("03", 16))){//iconst_0
            javaCodeStack.push("0");
        }else if(opcode.equals(Integer.parseInt("04", 16))){//iconst_1
            javaCodeStack.push("1");
        }else if(opcode.equals(Integer.parseInt("05", 16))){//iconst_2
            javaCodeStack.push("2");
        }else if(opcode.equals(Integer.parseInt("06", 16))){//iconst_3
            javaCodeStack.push("3");
        }else if(opcode.equals(Integer.parseInt("07", 16))){//iconst_4
            javaCodeStack.push("4");
        }else if(opcode.equals(Integer.parseInt("08", 16))){//iconst_5
            javaCodeStack.push("5");
        }else if(opcode.equals(Integer.parseInt("1c", 16))){//iload_2
            javaCodeStack.push(addVariables[2]);
        }else if(opcode.equals(Integer.parseInt("1d", 16))){//iload_3
            javaCodeStack.push(addVariables[3]);
        }else if(opcode.equals(Integer.parseInt("60", 16))){//iadd
            pushCalc(javaCodeStack, javaCodeStack.pop(), javaCodeStack.pop(), " + ");//iadd
        }else if(opcode.equals(Integer.parseInt("3e", 16))){//istore_3
            javaCodeStack.push("int " + addVariables[3] + " = " + javaCodeStack.pop());
        }else if(opcode.equals(Integer.parseInt("3d", 16))){//istore_2
            javaCodeStack.push("int " + addVariables[23] + " = " + javaCodeStack.pop());
        }else if(opcode.equals(Integer.parseInt("3c", 16))){//istore_1
            javaCodeStack.push("int " + addVariables[1] + " = " + javaCodeStack.pop());
        }else if(opcode.equals(Integer.parseInt("3b", 16))){//istore_0
            javaCodeStack.push("int " + addVariables[0] + " = " + javaCodeStack.pop());
        }else if(opcode.equals(Integer.parseInt("68", 16))){//imul
            pushCalc(javaCodeStack, javaCodeStack.pop(), javaCodeStack.pop(), " * ");//imul
        }else if(opcode.equals(Integer.parseInt("36", 16))){//istore
            javaCodeStack.push("int " + addVariables[operand] + " = " + javaCodeStack.pop());
        }else if(opcode.equals(Integer.parseInt("15", 16))){//iload
            javaCodeStack.push(addVariables[operand]);
        }else if(opcode.equals(Integer.parseInt("ac", 16))){//ireturn
            javaCodeStack.push("return " + javaCodeStack.pop());//ireturn
        }

        return javaCodeStack;
    }
    public static Stack<String> pushCalc(Stack<String> stack, String second, String first, String operateType) {
        stack.push(first + operateType + second);
        return stack;
    }

    private static int analysisAttribute(int offset, ArrayList<AttributeInfo> attributes) {
        int attributeNameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("attribute_name_index:#" + attributeNameIndex + "->" + constantMap.get(attributeNameIndex));
        int attributeLength = ByteUtils.bytes2Int(classByte, offset + u2, u4);
        offset += u4 + u2;
        System.out.println("attribute_length:" + attributeLength);

        if (constantMap.get(attributeNameIndex).equals("Code")) {//解析Code属性
            System.out.println("====================Code START=======================");
            CodeAttribute codeAttribute = new CodeAttribute();
            attributes.add(codeAttribute);
            offset = analysisCodeAttr(offset, codeAttribute);
            System.out.println("====================Code END=======================");
        } else if (constantMap.get(attributeNameIndex).equals("LineNumberTable")) {
            offset = analysisLineNumberTable(offset);
        } else if (constantMap.get(attributeNameIndex).equals("LocalVariableTable")) {
            LocalVariableTable localVariableTable = new LocalVariableTable();
            attributes.add(localVariableTable);
            offset = analysisLocalVariableTable(offset, localVariableTable);
        } else if (constantMap.get(attributeNameIndex).equals("ConstantValue")) {
            int constantvalueIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            attributes.add(new ConstantValue(constantvalueIndex));
            offset += u2;
            System.out.println("constantvalue_index:#" + constantvalueIndex + " -> " + constantMap.get(constantvalueIndex));
        } else {
            // TODO: 2017/9/23 属性解析
            offset += attributeLength;
        }
        return offset;
    }

    private static int analysisLineNumberTable(int offset) {
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
        return offset;
    }

    private static int analysisLocalVariableTable(int offset, LocalVariableTable localVariableTable) {
        int localVariableTableLength = ByteUtils.bytes2Int(classByte, offset, u2);
        localVariableTable.setLocalVariableTableLength(localVariableTableLength);
        ArrayList<LocalVariableInfo> localVariables = new ArrayList<>(localVariableTableLength);
        localVariableTable.setLocalVariableTable(localVariables);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!LocalVariableTable START!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        System.out.println("local_variable_table_length:" + localVariableTableLength);

        if(localFlag){
            addVariables = new String[localVariableTableLength];
        }
        offset += u2;
        for (int i = 0; i < localVariableTableLength; i++) {
            LocalVariableInfo localVariableInfo = new LocalVariableInfo();
            localVariables.add(localVariableInfo);
            int startPc = ByteUtils.bytes2Int(classByte, offset, u2);
            localVariableInfo.setStartPc(startPc);
            offset += u2;
            int length = ByteUtils.bytes2Int(classByte, offset, u2);
            localVariableInfo.setLength(length);
            offset += u2;
            int nameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            localVariableInfo.setNameIndex(nameIndex);
            offset += u2;
            int descriptorIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            localVariableInfo.setDescriptorIndex(descriptorIndex);
            offset += u2;
            int index = ByteUtils.bytes2Int(classByte, offset, u2);
            localVariableInfo.setIndex(index);
            if(localFlag) {
                addVariables[index] = constantMap.get(nameIndex);
            }
            offset += u2;
            System.out.println("----------Variable" + i + "-----------");
            System.out.println("start_pc:" + startPc);
            System.out.println("length:" + length);
            System.out.println("name_index:#" + nameIndex + "->" + constantMap.get(nameIndex));
            System.out.println("descriptor_index:#" + descriptorIndex + "->" + constantMap.get(descriptorIndex));
            System.out.println("index:" + index);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!LocalVariableTable END!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        return offset;
    }

    private static int analysisClassIndex(int offset, ClassFile classFile) {
        int thisClassIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int superClassIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        classFile.setThisClass(thisClassIndex);
        classFile.setSuperClass(superClassIndex);
        System.out.println("this_class_index:#" + thisClassIndex + "->" + constantMap.get(thisClassIndex));
        System.out.println("super_class_index:#" + superClassIndex + "->" + constantMap.get(superClassIndex));
        offset += u4;

        int interfacesCount = ByteUtils.bytes2Int(classByte, offset, u2);
        classFile.setInterfacesCount(interfacesCount);
        System.out.println("interfaces_count:" + interfacesCount);

        int[] interfaces = new int[interfacesCount];
        offset += u2;
        for (int i = 0; i < interfacesCount; i++) {
            int interfaceIndex = ByteUtils.bytes2Int(classByte, offset, u2);
            interfaces[i] = interfaceIndex;
            System.out.println("interface_index:#" + interfaceIndex + "->" + constantMap.get(interfaceIndex));
            offset += u2;
        }
        classFile.setInterfaces(interfaces);
        return offset;
    }

    private static int analysisConstantPool(int offset, ClassFile classFile) {
        /* 常量池大小 */
        int cpc = ByteUtils.bytes2Int(classByte, offset, u2);
        constantMap = new HashMap<>(cpc);
        constantIndexMap = new HashMap<>(cpc);
        classFile.setConstantPoolCount(cpc);
        System.out.println("constant_pool_count:" + cpc);
        System.out.println("============================常量池start===================================");
        offset += u2;
        ConstantPoolInfo[] constantPool = new ConstantPoolInfo[cpc];
        classFile.setConstantPool(constantPool);
        for (int i = 1; i < cpc; i++) {
            int tag = ByteUtils.bytes2Int(classByte, offset, u1);
            System.out.println("-------------------------------" + i + "---------------------------------");
            System.out.println("tag:" + tag);
            offset += u1;
            switch (tag) {
                case ContantItem.UTF8_TAG:
                    offset = analysisConstantUtf8(constantPool, offset, i);
                    break;
                case ContantItem.INTEGER_TAG:
                    offset = analysisConstantInteger(constantPool, offset, i);
                    break;
                case ContantItem.FLOAT_TAG:
                    offset = analysisConstantFloat(constantPool, offset, i);
                    break;
                case ContantItem.LONG_TAG:
                    offset = analysisConstantLong(constantPool, offset, i);
                    ++i;
                    break;
                case ContantItem.DOUBLE_TAG:
                    offset = analysisConstantDouble(constantPool, offset, i);
                    ++i;
                    break;
                case ContantItem.CLASS_TAG:
                    offset = analysisConstantClass(constantPool, offset, i);
                    break;
                case ContantItem.STRING_TAG:
                    offset = analysisConstantString(constantPool, offset, i);
                    break;
                case ContantItem.FIELDREF_TAG:
                    offset = analysisConstantFieldref(constantPool, offset, i);
                    break;
                case ContantItem.METHODREF_TAG:
                    offset = analysisConstantMethodRef(constantPool, offset, i);
                    break;
                case ContantItem.INTERFACEMETHODREF_TAG:
                    offset = analysisConstantInterfaceMethodRef(constantPool, offset, i);
                    break;
                case ContantItem.NAMEANDTYPE_TAG:
                    offset = analysisConstantNameAndType(constantPool, offset, i);
                    break;
                case ContantItem.METHODHANDLE_TAG:
                    offset = analysisConstantMethodHandle(constantPool, offset, i);
                    break;
                case ContantItem.METHODTYPE_TAG:
                    offset = analysisConstantMethodType(constantPool, offset, i);
                    break;
                case ContantItem.INVOKEDYNAMIC_TAG:
                    offset = analysisConstantInvokeDynamic(constantPool, offset, i);
                    break;
                default:
                    System.out.println(tag + "**************************" + tag);
            }
        }
        System.out.println("==============================常量池end=================================");
        return offset;
    }

    private static int analysisConstantInvokeDynamic(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int bootstarpMethodAttrIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int nameAndTypeIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("InvokeDynamic_bootstarp_method_attr_index:#" + bootstarpMethodAttrIndex);
        System.out.println("InvokeDynamic_name_and_type_index:#" + nameAndTypeIndex);
        constantPool[index] = new ConstantInvokeDynamicInfo(bootstarpMethodAttrIndex, nameAndTypeIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.INVOKEDYNAMIC_TAG, String.valueOf(bootstarpMethodAttrIndex) + ":" + String.valueOf(nameAndTypeIndex)));
        return offset + u4;
    }

    private static int analysisConstantMethodType(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("methodType_descriptor_index:#" + descriptorIndex);
        constantPool[index] = new ConstantMethodTypeInfo(descriptorIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODTYPE_TAG, String.valueOf(descriptorIndex)));
        return offset + u2;
    }

    private static int analysisConstantMethodHandle(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int referenceKind = ByteUtils.bytes2Int(classByte, offset, u1);
        int referenceIndex = ByteUtils.bytes2Int(classByte, offset + u1, u2);
        System.out.println("methodHandle_reference_kind:" + referenceKind);
        System.out.println("methodHandle_reference_index:#" + referenceIndex);
        constantPool[index] = new ConstantMethodHandleInfo(referenceKind, referenceIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODHANDLE_TAG, String.valueOf(referenceKind) + ":" + String.valueOf(referenceIndex)));
        return offset + u1 + u2;
    }

    private static int analysisConstantNameAndType(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int nameIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int typeIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("nameAndType_name_index:#" + nameIndex);
        System.out.println("nameAndType_type_index:#" + typeIndex);
        constantPool[index] = new ConstantNameAndTypeInfo(nameIndex, typeIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.NAMEANDTYPE_TAG, String.valueOf(nameIndex) + ":" + String.valueOf(typeIndex)));
        return offset + u4;
    }

    private static int analysisConstantInterfaceMethodRef(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("interfaceMethod_class_index:#" + classIndex);
        System.out.println("interfaceMethod_descriptor_index:#" + descriptorIndex);
        constantPool[index] = new ConstantInterfaceMethodrefInfo(classIndex, descriptorIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.INTERFACEMETHODREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantMethodRef(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("method_class_index:#" + classIndex);
        System.out.println("method_descriptor_index:#" + descriptorIndex);
        constantPool[index] = new ConstantMethodrefInfo(classIndex, descriptorIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.METHODREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantFieldref(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        int descriptorIndex = ByteUtils.bytes2Int(classByte, offset + u2, u2);
        System.out.println("field_class_index:#" + classIndex);
        System.out.println("field_descriptor_index:#" + descriptorIndex);
        constantPool[index] = new ConstantFieldrefInfo(classIndex, descriptorIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.FIELDREF_TAG, String.valueOf(classIndex) + ":" + String.valueOf(descriptorIndex)));
        return offset + u4;
    }

    private static int analysisConstantString(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int stringIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("string_index:#" + stringIndex);
        constantPool[index] = new ConstantStringInfo(stringIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.STRING_TAG, String.valueOf(stringIndex)));
        return offset + u2;
    }

    private static int analysisConstantClass(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int classIndex = ByteUtils.bytes2Int(classByte, offset, u2);
        System.out.println("class_index:#" + classIndex);
        constantPool[index] = new ConstantClassInfo(classIndex);
        constantIndexMap.put(index, new ContantIndex(false, ContantItem.CLASS_TAG, String.valueOf(classIndex)));
        return offset + u2;
    }

    private static int analysisConstantDouble(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        double doubleValue = ByteUtils.bytes2Double1(classByte, offset);
        System.out.println("double_value:" + doubleValue);
        constantPool[index] = new ConstantDoubleInfo(doubleValue);
        constantIndexMap.put(index, new ContantIndex(ContantItem.DOUBLE_TAG, String.valueOf(doubleValue)));
        return offset + u8;
    }

    private static int analysisConstantLong(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        long longValue = ByteUtils.bytes2Long1(classByte, offset);
        System.out.println("long_value:" + longValue);
        constantPool[index] = new ConstantLongInfo(longValue);
        constantIndexMap.put(index, new ContantIndex(ContantItem.LONG_TAG, String.valueOf(longValue)));
        return offset + u8;
    }

    private static int analysisConstantFloat(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        float floatValue = ByteUtils.bytes2Float1(classByte, offset);
        System.out.println("float_value:" + floatValue);
        constantPool[index] = new ConstantFloatInfo(floatValue);
        constantIndexMap.put(index, new ContantIndex(ContantItem.FLOAT_TAG, String.valueOf(floatValue)));
        return offset + u4;
    }

    private static int analysisConstantInteger(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        int intValue = ByteUtils.bytes2Int(classByte, offset, u4);
        System.out.println("integer_value:" + intValue);
        constantPool[index] = new ConstantIntegerInfo(intValue);
        constantIndexMap.put(index, new ContantIndex(ContantItem.INTEGER_TAG, String.valueOf(intValue)));
        return offset + u4;
    }

    private static int analysisConstantUtf8(ConstantPoolInfo[] constantPool, Integer offset, int index) {
        ConstantUtf8Info utf8Info = new ConstantUtf8Info();
        int length = ByteUtils.bytes2Int(classByte, offset, u2);
        offset += u2;
        utf8Info.setLength(length);
        System.out.println("constant_length:" + length);
        String constantValue = ByteUtils.bytes2String(classByte, offset, length);
        utf8Info.setBytes(constantValue);
        System.out.println("utf8_value:" + constantValue);
        constantPool[index] = utf8Info;
        constantIndexMap.put(index, new ContantIndex(ContantItem.UTF8_TAG, constantValue));
        return offset + length;
    }
}
