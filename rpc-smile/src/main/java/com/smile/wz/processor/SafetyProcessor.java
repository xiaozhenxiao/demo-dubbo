package com.smile.wz.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * wangzhen23
 * 2018/11/16.
 */
@SupportedAnnotationTypes({"com.smile.wz.processor.Safety"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SafetyProcessor extends AbstractProcessor {
    /**
     * Messager主要是用来在编译期打log用的
     */
    private Messager messager;
    /**
     * JavacTrees提供了待处理的抽象语法树
     */
    private JavacTrees trees;
    /**
     * TreeMaker封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;
    /**
     * Names提供了创建标识符的方法
     */
    private Names names;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Safety.class);
        System.out.println("safety set:" + set);
        set.forEach(element -> {
            if (element.getKind() == ElementKind.CLASS) {
                System.out.println("process all");
                processClass(element);
            }else {
                processMethod(element);
            }
        });

        return true;
    }

    private void processClass(Element element) {
        element.getEnclosedElements().forEach(method -> {
            if (method.getKind() == ElementKind.METHOD && !method.getModifiers().contains(Modifier.STATIC) &&
                    !method.getModifiers().contains(Modifier.DEFAULT)) {
                JCTree jcTree = trees.getTree(method);
                jcTree.accept(new TreeTranslator() {
                    @Override
                    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                        List<JCTree.JCStatement> jcStatementList = List.nil();
                        // TODO: 2018/11/16
                        jcStatementList.append(treeMaker.Exec(
                                treeMaker.Apply(
                                        List.nil(),
                                        treeMaker.Select(
                                                treeMaker.Ident(getNameFromString("")),
                                                jcMethodDecl.getName()
                                        ),
                                        List.of(treeMaker.Ident(getNameFromString(""))) //传入的参数集合
                                )
                        ));
                        jcMethodDecl.body.stats.prependList(jcStatementList);
                        super.visitMethodDef(jcMethodDecl);
                    }

                });
            }});
    }

    private void processMethod(Element element) {
        if (element.getKind() == ElementKind.METHOD && !element.getModifiers().contains(Modifier.STATIC) &&
                !element.getModifiers().contains(Modifier.DEFAULT)) {

        }
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }
}
