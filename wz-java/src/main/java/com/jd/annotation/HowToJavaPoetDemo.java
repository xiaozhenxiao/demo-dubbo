package com.jd.annotation;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;

public class HowToJavaPoetDemo {
    public static void main(String[] args) throws IOException {
        // `JavaFile` 代表 Java 文件
        JavaFile javaFile = JavaFile.builder("com.wz.annotation",
                // TypeSpec 代表一个类
                TypeSpec.classBuilder("Clazz")
                        // 给类添加一个属性
                        .addField(FieldSpec.builder(int.class, "mField", Modifier.PRIVATE)
                                .build())
                        // 给类添加一个方法
                        .addMethod(MethodSpec.methodBuilder("method").addParameter(String.class, "str")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(void.class)
                                .addStatement("System.out.println(str)")
                                .build())
                        .build())
                .build();

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get("java.util", "List"), ClassName.get("java.lang", "String"));
        List<TypeName> typeArguments = parameterizedTypeName.typeArguments;
        TypeVariableName typeVariableName = TypeVariableName.get("T");

        javaFile.writeTo(System.out);
    }
}
