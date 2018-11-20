package com.smile.wz.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
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
import javax.tools.Diagnostic;
import java.util.Iterator;
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

    private String formatString = "参数:";

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
            } else {
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
                        List<JCTree.JCVariableDecl> params = jcMethodDecl.params;
                        List<JCTree.JCStatement> jcStatementList = List.nil();

                        JCTree.JCVariableDecl var = treeMaker.VarDef(
                                treeMaker.Modifiers(0),
                                getNameFromString("xiao"), //名字
                                memberAccess("java.lang.String"), //类型
                                treeMaker.Literal(jcMethodDecl.name.toString())//初始化语句
                        );//String xiao = "method"
                        jcStatementList = jcStatementList.append(var);

                        JCTree.JCExpressionStatement es = treeMaker.Exec(treeMaker.Apply(
                                List.of(memberAccess("java.lang.String")),//参数类型
                                memberAccess("java.lang.System.out.println"),
//                                List.of(treeMaker.Literal("xiao test zhen"))//参数集合
                                List.of(treeMaker.Ident(getNameFromString("xiao")))
                                )
                        );//System.out.println(xiao);
                        jcStatementList = jcStatementList.append(es);

                        if (params.length() > 0) {
                            List<JCTree.JCExpression> args = List.nil();
//                            String outString = "参数：";
                            for (int i = 0; i < params.length(); i++) {
                                Type p = params.get(i).getType().type;

                                args = processNestingParameter(p, args, params.get(i).name.toString());
                                if (i == params.length() - 1) {
                                    formatString += "\n\r";
                                }
                            }
                            args = args.prepend(treeMaker.Literal(formatString));
                            JCTree.JCExpressionStatement paramES = treeMaker.Exec(treeMaker.Apply(
                                    List.nil(),//参数类型
                                    memberAccess("java.lang.System.out.printf"),
//                                List.of(treeMaker.Literal("xiao test zhen"))//参数集合
                                    args
                                    )
                            );
                            jcStatementList = jcStatementList.append(paramES);
                        }

                        List<JCTree.JCStatement> methodBlock = jcMethodDecl.body.stats.prependList(jcStatementList);
                        jcMethodDecl.body = treeMaker.Block(0, methodBlock);
                        super.visitMethodDef(jcMethodDecl);
                        formatString = "参数:";
                    }

                });
            }
        });
    }

    private List<JCTree.JCExpression> processNestingParameter(Type p, List<JCTree.JCExpression> args, String name) {
        System.out.println("====" + p);
        if (isPrimitive(p)) {
            args = args.append(memberAccess(name));
            formatString += name + ":%s,";
        } else {
            Iterator<Symbol> pmember = ((Symbol.ClassSymbol) p.tsym).members_field.getElements().iterator();
            String[] names = name.split("\\.");
            String prefix;
            boolean nesting = true;
            if (names.length > 1) {
                prefix = names[0];
                for (int j = 1; j < names.length; j++) {
                    prefix += ".get" + getUpperCaseFirst(names[j]);
                }
            } else {
                prefix = name;
                nesting = false;
            }
            while (pmember.hasNext()) {
                Symbol member = pmember.next();
                if (member instanceof Symbol.VarSymbol) {
                    System.out.println("p.field:" + member.flatName());
                    System.out.println("p.field.type:" + member.asType());
                    if (isPrimitive(member.asType())) {
                        JCTree.JCExpression fn;
                        if (nesting) {
                            fn = functionAccess(prefix + ".get" + getUpperCaseFirst(member.flatName().toString()));
                        } else {
                            fn = treeMaker.Apply(
                                    List.nil(),//参数类型
                                    memberAccess(prefix + ".get" + getUpperCaseFirst(member.flatName().toString())),
                                    List.nil()
                            );
                        }
                        args = args.append(fn);
                        formatString += name + "." + member.flatName() + ":%s,";
                    } else {
                        args = processNestingParameter(member.asType(), args, name + "." + member.flatName());// TODO: 2018/11/20
                    }
                }
            }
        }
        return args;
    }

    private boolean isPrimitive(Type t) {
        return t.isPrimitive() || "java.lang.String".equals(t.toString()) || "java.lang.Integer".equals(t.toString())
                || "java.lang.Byte".equals(t.toString()) || "java.lang.Short".equals(t.toString()) || "java.lang.Long".equals(t.toString())
                || "java.lang.Character".equals(t.toString()) || "java.lang.Boolean".equals(t.toString()) || "java.lang.Float".equals(t.toString())
                || "java.lang.Double".equals(t.toString());
    }

    private String getUpperCaseFirst(String flatName) {
        String first = flatName.substring(0, 1).toUpperCase();
        return first + flatName.substring(1);
    }

    private void processMethod(Element element) {
        if (element.getKind() == ElementKind.METHOD && !element.getModifiers().contains(Modifier.STATIC) &&
                !element.getModifiers().contains(Modifier.DEFAULT)) {

        }
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

    private JCTree.JCExpressionStatement assignment(Name name) {
        return treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("this")),
                                name
                        ),
                        treeMaker.Ident(name)
                )
        );
    }

    private JCTree.JCVariableDecl makeVarDef(JCTree.JCVariableDecl prototypeJCVariable) {
        return treeMaker.VarDef(
                prototypeJCVariable.mods,
                prototypeJCVariable.name, //名字
                prototypeJCVariable.vartype, //类型
                null //初始化语句
        );
    }

    /**
     * 创建 域/方法 的多级访问, 方法的标识只能是最后一个
     * 例如： java.lang.System.out.println
     *
     * @param components 例  java.lang.System.out.println
     * @return
     */
    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    /**
     * 创建函数的多级调用，函数都没有参数，例如： person.getSon().getName()
     *
     * @param components
     * @return
     */
    private JCTree.JCExpression functionAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Apply(
                    List.nil(),
                    treeMaker.Select(expr, getNameFromString(componentArray[i])),
                    List.nil());
        }
        return expr;
    }
}
