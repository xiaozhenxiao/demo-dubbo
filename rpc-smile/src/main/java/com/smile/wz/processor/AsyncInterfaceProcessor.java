package com.smile.wz.processor;

import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

/**
 * wangzhen23
 * 2018/3/28.
 */
@SupportedAnnotationTypes({
        "com.smile.wz.processor.Async"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AsyncInterfaceProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;
    private static boolean isGenerate = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Processing " + annotations + roundEnv);
        roundEnv.getElementsAnnotatedWith(Async.class).forEach(element -> {
            try {
                if (element.getKind() != ElementKind.INTERFACE) {
                    System.out.println("not interface");
                    return;
                }
                String elementType = element.asType().toString();
                TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(element.getSimpleName() + "Async").addModifiers(Modifier.PUBLIC);
                element.getEnclosedElements().forEach(method -> {
                    if (method.getKind() == ElementKind.METHOD && !method.getModifiers().contains(Modifier.STATIC) &&
                            !method.getModifiers().contains(Modifier.DEFAULT)) {
                        /*System.out.println("method:" + method);
                        System.out.println("method name:" + method.getSimpleName());
                        System.out.println("method modifier:" + method.getModifiers());*/
                        TypeMirror methodType = method.asType();
                        String[] methodTypes = methodType.toString().split("\\)");

                        String[] parameters = methodTypes[0].substring(1).split(",");
                        /*for (String parameter : parameters) {
                            String parameterSimpleName = parameter.substring(parameter.lastIndexOf(".") + 1);
                            String parameterPackage = parameter.substring(0, parameter.lastIndexOf("."));
                            System.out.println("parameterPackage:" + parameterPackage);
                            System.out.println("parameterSimpleName:" + parameterSimpleName);
                        }*/

                        String returnType = methodTypes[1];
                        String returnSimpleName = returnType.substring(returnType.lastIndexOf(".") + 1);
                        String returnPackage = returnType.substring(0, returnType.lastIndexOf(".") < 0 ? 0 : returnType.lastIndexOf("."));
//                        System.out.println("return type:" + returnType);
//                        System.out.println("returnPackage:" + returnPackage);
//                        System.out.println("returnSimpleName:" + returnSimpleName);

                        MethodSpec.Builder builderMethod = MethodSpec.methodBuilder(method.getSimpleName().toString())
                                .returns(getTypeName(returnSimpleName) != null ? getTypeName(returnSimpleName) : ClassName.get(returnPackage, returnSimpleName))
                                .addModifiers(method.getModifiers());

                        MethodSpec.Builder builderMethodAsync = MethodSpec.methodBuilder(method.getSimpleName().toString() + "Async")
                                .returns(ParameterizedTypeName.get(ClassName.get("java.util.concurrent", "Future"), getTypeName(returnSimpleName) != null ? getTypeName(returnSimpleName).box() : ClassName.get(returnPackage, returnSimpleName)))
                                .addModifiers(method.getModifiers());
                        int i = 0;
                        for (String parameter : parameters) {
                            if (parameter == null || parameter.length() < 1) continue;
                            String parameterSimpleName = parameter.substring(parameter.lastIndexOf(".") + 1);
                            String parameterPackage = parameter.substring(0, parameter.lastIndexOf("."));
                            builderMethod.addParameter(getTypeName(parameterSimpleName) != null ? getTypeName(parameterSimpleName) : ClassName.get(parameterPackage, parameterSimpleName), lowerFirstChar(parameterSimpleName) + ++i);
                            builderMethodAsync.addParameter(getTypeName(parameterSimpleName) != null ? getTypeName(parameterSimpleName) : ClassName.get(parameterPackage, parameterSimpleName), lowerFirstChar(parameterSimpleName) + ++i);
                        }
                        typeBuilder.addMethod(builderMethod.build());
                        typeBuilder.addMethod(builderMethodAsync.build());
                    }
                });
                TypeSpec typeSpec = typeBuilder.build();
                try {
                    if (!isGenerate) {
//                        JavaFile.builder(elementType.substring(0, elementType.lastIndexOf(".")), typeSpec).build().writeTo(System.out);
                        JavaFile.builder(elementType.substring(0, elementType.lastIndexOf(".")), typeSpec).build().writeTo(filer);
                        isGenerate = true;
                    }
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "JavaFile builder" + e.getMessage());
                }
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        });
        return true;
    }

    private static String lowerFirstChar(String name) {
        if (name.length() < 1) {
            return name;
        }
        String firstChar = name.substring(0, 1).toLowerCase();
        if (name.length() > 1) {
            return firstChar + name.substring(1);
        }
        return firstChar;
    }

    private TypeName getTypeName(String type) {
        switch (type) {
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "int":
                return TypeName.INT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
            case "void":
                return TypeName.VOID;
            default:
                return null;
        }
    }
}
