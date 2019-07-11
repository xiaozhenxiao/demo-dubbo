package com.smile.wz.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Iterator;
import java.util.Objects;
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

    private List<String> processMethods = List.nil();

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
        set.forEach(element -> {
            if (element.getKind() == ElementKind.CLASS) {
                processClass(element);
            } else {
                processMethod(element);
            }
        });

        return true;
    }

    private void processClass(Element element) {
        JCTree jcClassTree = trees.getTree(element);
        element.getEnclosedElements().forEach(method -> {
            if (method.getKind() == ElementKind.METHOD && !method.getModifiers().contains(Modifier.STATIC) &&
                    !method.getModifiers().contains(Modifier.DEFAULT)) {
                processMethod(jcClassTree, method);
            }
        });
    }

    private void processMethod(JCTree jcClassTree, Element method) {
        Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) method;
        if (processMethods.contains(methodSymbol.owner + "." + methodSymbol.name + methodSymbol.type)) {
            return;
        }
        processMethods = processMethods.append(methodSymbol.owner + "." + methodSymbol.name + methodSymbol.type);
        JCTree jcMethodTree = trees.getTree(method);

        jcMethodTree.accept(new TreeTranslator() {
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                ListBuffer<String> excludes = new ListBuffer();
                List<JCTree.JCAnnotation> annotations = jcMethodDecl.mods.annotations;
                if (!Objects.isNull(annotations)) {
                    annotations.forEach(annotation -> {
                        if ("com.smile.wz.processor.Safety".equals(annotation.type.toString())) {
                            List<JCTree.JCExpression> annArgs = annotation.args;
                            annArgs.forEach(arg -> {
                                if ("exclude".equals(((JCTree.JCAssign) arg).lhs.toString())) {
                                    ((JCTree.JCNewArray) ((JCTree.JCAssign) arg).rhs).elems.forEach(exclude -> excludes.add(exclude.toString().replaceAll("\"","")));
                                }
                            });
                        }
                    });
                }
                formatString = "参数:";
                JCTree.JCMethodDecl generateMethod = treeMaker.MethodDef(
//                                treeMaker.Modifiers(jcMethodDecl.sym.flags(), treeMaker.Annotations(jcMethodDecl.sym.getRawAttributes())),
                        jcMethodDecl.mods,
                        getNameFromString("__" + jcMethodDecl.name),
//                                treeMaker.Type(jcMethodDecl.sym.getReturnType()),
                        jcMethodDecl.restype,
//                                treeMaker.TypeParams(jcMethodDecl.sym.type.getTypeArguments()),
                        jcMethodDecl.typarams,
//                                treeMaker.Params(jcMethodDecl.sym.type.getParameterTypes(), jcMethodDecl.sym),
                        jcMethodDecl.params,
//                                treeMaker.Types(jcMethodDecl.sym.type.getThrownTypes()),
                        jcMethodDecl.thrown,
                        treeMaker.Block(0, getJcStatements(jcMethodDecl, excludes)),
                        jcMethodDecl.defaultValue
                );
                jcClassTree.accept(new TreeTranslator() {
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        jcClassDecl.defs = jcClassDecl.defs.append(generateMethod);
                        super.visitClassDef(jcClassDecl);
                    }
                });
                List<JCTree.JCVariableDecl> paramList = jcMethodDecl.params;
                List<JCTree.JCExpression> params = List.nil();
                if (paramList.length() > 0) {
                    for (int i = 0; i < paramList.length(); i++) {
                        params = params.append(treeMaker.Ident(paramList.get(i).name));
                    }
                }

                List<JCTree.JCStatement> jcStatementList = List.nil();

                if (!jcMethodDecl.restype.toString().equals("void")) {
                    JCTree.JCVariableDecl returnVar = makeVarDef(treeMaker.Modifiers(0), "__result", jcMethodDecl.restype,
                            treeMaker.Apply(List.nil(),
                                    treeMaker.Select(treeMaker.Ident(names.fromString("this")), getNameFromString("__" + jcMethodDecl.name)),
                                    params
                            ));
                    jcStatementList = jcStatementList.append(returnVar);
                    List<JCTree.JCExpression> args = List.nil();
                    formatString = "结果:";
                    args = processNestingType(returnVar.vartype.type, args, "__result", excludes);
                    formatString += "\n\r";
                    jcStatementList = constructPrintStatements(jcStatementList, args);
                } else {
                    jcStatementList = jcStatementList.append(treeMaker.Exec(treeMaker.Apply(
                            List.nil(),//参数类型
                            treeMaker.Select(treeMaker.Ident(names.fromString("this")), getNameFromString("__" + jcMethodDecl.name)),
                            params
                    )));
                }
                jcMethodDecl.body = treeMaker.Block(0, jcMethodDecl.body.stats.prependList(jcStatementList));
                super.visitMethodDef(jcMethodDecl);

            }

        });
    }

    private List<JCTree.JCStatement> constructPrintStatements(List<JCTree.JCStatement> jcStatementList, List<JCTree.JCExpression> args) {
        args = args.prepend(treeMaker.Literal(formatString));
        JCTree.JCExpressionStatement printStatement = treeMaker.Exec(treeMaker.Apply(
                List.nil(),//参数类型
                memberAccess("java.lang.System.out.printf"),
                args
                )
        );
        return jcStatementList.append(printStatement);
    }

    private List<JCTree.JCStatement> getJcStatements(JCTree.JCMethodDecl jcMethodDecl, ListBuffer<String> excludes) {
        List<JCTree.JCVariableDecl> params = jcMethodDecl.params;

        List<JCTree.JCStatement> jcStatementList = List.nil();

        //String xiao = "methodName";  初始化变量并赋初始值
        JCTree.JCVariableDecl var = makeVarDef(treeMaker.Modifiers(0), "xiao", memberAccess("java.lang.String"), treeMaker.Literal(jcMethodDecl.name.toString()));
        jcStatementList = jcStatementList.append(var);

        //xiao = "assignment test";  给变量赋值
        JCTree.JCExpressionStatement assignment = makeAssignment(treeMaker.Ident(getNameFromString("xiao")), treeMaker.Literal(jcMethodDecl.name.toString() + " assignment test"));
        jcStatementList = jcStatementList.append(assignment);

        //xiao = "-Binary operator one" + "-Binary operator two";  两个字符串字面量相加并赋值
        JCTree.JCExpressionStatement binaryStatement = treeMaker.Exec(
                treeMaker.Assign(treeMaker.Ident(getNameFromString("xiao")),
                        treeMaker.Binary(
                                JCTree.Tag.PLUS,
                                treeMaker.Literal("-Binary operator one"),
                                treeMaker.Literal("-Binary operator two")
                        ))
        );
        jcStatementList = jcStatementList.append(binaryStatement);

        //xiao += "Assignop test";
        JCTree.JCExpressionStatement assignmentSelf = treeMaker.Exec(
                treeMaker.Assignop(
                        JCTree.Tag.PLUS_ASG,
                        treeMaker.Ident(getNameFromString("xiao")),
                        treeMaker.Literal("-Assignop test")
                )
        );
        jcStatementList = jcStatementList.append(assignmentSelf);

        //Integer zhen = 1;
        JCTree.JCVariableDecl intVar = makeVarDef(treeMaker.Modifiers(0), "zhen", memberAccess("java.lang.Integer"), treeMaker.Literal(1));
        jcStatementList = jcStatementList.append(intVar);
        //zhen++;
        JCTree.JCExpressionStatement unaryStatement = treeMaker.Exec(
                treeMaker.Unary(
                        JCTree.Tag.PREINC,
                        treeMaker.Ident(getNameFromString("zhen"))
                )
        );
        jcStatementList = jcStatementList.append(unaryStatement);

        //zhen = zhen + 10;
        JCTree.JCExpressionStatement binaryIntStatement = treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Ident(getNameFromString("zhen")),
                        treeMaker.Binary(
                                JCTree.Tag.PLUS,
                                treeMaker.Ident(getNameFromString("zhen")),
                                treeMaker.Literal(10)
                        ))
        );
        jcStatementList = jcStatementList.append(binaryIntStatement);

        /*JCTree.JCExpressionStatement printVar = treeMaker.Exec(treeMaker.Apply(
                List.of(memberAccess("java.lang.String")),//参数类型
                memberAccess("java.lang.System.out.println"),
                List.of(treeMaker.Ident(getNameFromString("xiao")))
                )
        );*///System.out.println(xiao);

        /*JCTree.JCExpressionStatement printLiteral = treeMaker.Exec(treeMaker.Apply(
                List.of(memberAccess("java.lang.String")),//参数类型
                memberAccess("java.lang.System.out.println"),
                List.of(treeMaker.Literal("xiao test zhen"))//参数集合
                )
        );*///System.out.println("xiao test zhen");

        //创建if语句
        /**
         * if (zhen < 10) {
         *     System.out.println(xiao);
         * } else {
         *     System.out.println("xiao test zhen");
         * }
         * **/
        /*JCTree.JCIf jcIf = treeMaker.If(
                treeMaker.Binary(
                        JCTree.Tag.LT,
                        treeMaker.Ident(getNameFromString("zhen")),
                        treeMaker.Literal(10)
                ),
                printVar,
                printLiteral
        );
        jcStatementList = jcStatementList.append(jcIf);
*/
        /**
         * if(xiao != null){
         *      System.out.println(xiao);
         * } else {
         *      System.out.println("xiao test zhen");
         * }
         */
        /*JCTree.JCIf jcIfNull = treeMaker.If(
                treeMaker.Parens(
                        treeMaker.Binary(
                                JCTree.Tag.NE,
                                treeMaker.Ident(getNameFromString("xiao")),
                                treeMaker.Literal(TypeTag.BOT, null))
                ),
                printVar,
                printLiteral
        );
        jcStatementList = jcStatementList.append(jcIfNull);*/

        if (params.length() > 0) {
            List<JCTree.JCExpression> args = List.nil();
            for (int i = 0; i < params.length(); i++) {
                Type p = params.get(i).getType().type;

                args = processNestingType(p, args, params.get(i).name.toString(), excludes);
                if (i == params.length() - 1) {
                    formatString += "\n\r";
                }
            }
            jcStatementList = constructPrintStatements(jcStatementList, args);
        }
        jcStatementList = jcMethodDecl.body.stats.prependList(jcStatementList);
        return jcStatementList;
    }

    private List<JCTree.JCExpression> processNestingType(Type p, List<JCTree.JCExpression> args, String name, ListBuffer<String> excludes) {
        if (isPrimitive(p)) {
            String[] names = name.split("\\.");
            if (excludes.contains(names[names.length - 1])) {
                return args;
            }
            String logName = NameAnalysis.analysis(names[names.length - 1]);
            if (!Objects.isNull(logName)) {
                args = args.append(memberAccess(name));
                formatString += logName + ":%s,";
            }
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
                    if (isPrimitive(member.asType())) {
                        if (excludes.contains(member.name.toString())) {
                            return args;
                        }
                        String logName = NameAnalysis.analysis(member.name.toString());
                        if (!Objects.isNull(logName)) {
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
                            formatString += logName + ":%s,";
                        }
                    } else {
                        args = processNestingType(member.asType(), args, name + "." + member.flatName(), excludes);// TODO: 2018/11/20
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
            Symbol clazz = ((Symbol.MethodSymbol) element).owner;
            JCTree jcClassTree = trees.getTree(clazz);
            processMethod(jcClassTree, element);
        }
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

    /**
     * 创建赋值语句
     *
     * @param lhs
     * @param rhs
     * @return
     */
    private JCTree.JCExpressionStatement makeAssignment(JCTree.JCExpression lhs, JCTree.JCExpression rhs) {
        return treeMaker.Exec(
                treeMaker.Assign(
                        lhs,
                        rhs
                )
        );
    }

    /**
     * 创建变量语句
     * modifiers vartype name = init;  例 (private) String xiao = "test";
     *
     * @param modifiers treeMaker.Modifiers(0)/treeMaker.Modifiers(Flags.PUBLIC)
     * @param name
     * @param vartype
     * @param init
     * @return
     */
    private JCTree.JCVariableDecl makeVarDef(JCTree.JCModifiers modifiers, String name, JCTree.JCExpression vartype, JCTree.JCExpression init) {
        return treeMaker.VarDef(
                modifiers,
                getNameFromString(name), //名字
                vartype, //类型
                init //初始化语句
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

    public void main(String[] args) {
//        Attribute.Compound compound = new Attribute.Compound(treeMaker.Type(Type.ClassType.));
    }
}