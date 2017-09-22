package com.jd.jvm.exec;

import java.util.Comparator;

/**
 * TODO
 * wangzhen23
 * 2017/9/20.
 */
public class DynamicInvoke {
    public static void main(String[] args) {
//        Comparator keyComparator = (c1, c2) -> -1;
    }
}
/** 字节码 **/
/*
Classfile /E:/myworkspaces/idea-dubbo/wz-java/target/classes/com/jd/jvm/exec/DynamicInvoke.class
  Last modified 2017-9-20; size 1127 bytes
  MD5 checksum 24fb4740ff51f8cdd24745735573d2f2
  Compiled from "DynamicInvoke.java"
public class com.jd.jvm.exec.DynamicInvoke
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #4.#25         // java/lang/Object."<init>":()V
   #2 = InvokeDynamic      #0:#30         // #0:compare:()Ljava/util/Comparator;
   #3 = Class              #31            // com/jd/jvm/exec/DynamicInvoke
   #4 = Class              #32            // java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Utf8               Code
   #8 = Utf8               LineNumberTable
   #9 = Utf8               LocalVariableTable
  #10 = Utf8               this
  #11 = Utf8               Lcom/jd/jvm/exec/DynamicInvoke;
  #12 = Utf8               main
  #13 = Utf8               ([Ljava/lang/String;)V
  #14 = Utf8               args
  #15 = Utf8               [Ljava/lang/String;
  #16 = Utf8               keyComparator
  #17 = Utf8               Ljava/util/Comparator;
  #18 = Utf8               lambda$main$0
  #19 = Utf8               (Ljava/lang/Object;Ljava/lang/Object;)I
  #20 = Utf8               c1
  #21 = Utf8               Ljava/lang/Object;
  #22 = Utf8               c2
  #23 = Utf8               SourceFile
  #24 = Utf8               DynamicInvoke.java
  #25 = NameAndType        #5:#6          // "<init>":()V
  #26 = Utf8               BootstrapMethods
  #27 = MethodHandle       #6:#33         // invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/
CallSite;
  #28 = MethodType         #19            //  (Ljava/lang/Object;Ljava/lang/Object;)I
  #29 = MethodHandle       #6:#34         // invokestatic com/jd/jvm/exec/DynamicInvoke.lambda$main$0:(Ljava/lang/Object;Ljava/lang/Object;)I
  #30 = NameAndType        #35:#36        // compare:()Ljava/util/Comparator;
  #31 = Utf8               com/jd/jvm/exec/DynamicInvoke
  #32 = Utf8               java/lang/Object
  #33 = Methodref          #37.#38        // java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #34 = Methodref          #3.#39         // com/jd/jvm/exec/DynamicInvoke.lambda$main$0:(Ljava/lang/Object;Ljava/lang/Object;)I
  #35 = Utf8               compare
  #36 = Utf8               ()Ljava/util/Comparator;
  #37 = Class              #40            // java/lang/invoke/LambdaMetafactory
  #38 = NameAndType        #41:#45        // metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #39 = NameAndType        #18:#19        // lambda$main$0:(Ljava/lang/Object;Ljava/lang/Object;)I
  #40 = Utf8               java/lang/invoke/LambdaMetafactory
  #41 = Utf8               metafactory
  #42 = Class              #47            // java/lang/invoke/MethodHandles$Lookup
  #43 = Utf8               Lookup
  #44 = Utf8               InnerClasses
  #45 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #46 = Class              #48            // java/lang/invoke/MethodHandles
  #47 = Utf8               java/lang/invoke/MethodHandles$Lookup
  #48 = Utf8               java/lang/invoke/MethodHandles
{
  public com.jd.jvm.exec.DynamicInvoke();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 10: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/jd/jvm/exec/DynamicInvoke;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=2, args_size=1
         0: invokedynamic #2,  0              // InvokeDynamic #0:compare:()Ljava/util/Comparator;
         5: astore_1
         6: return
      LineNumberTable:
        line 12: 0
        line 13: 6
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  args   [Ljava/lang/String;
            6       1     1 keyComparator   Ljava/util/Comparator;
}
SourceFile: "DynamicInvoke.java"
InnerClasses:
     public static final #43= #42 of #46; //Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
BootstrapMethods:
  0: #27 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #28 (Ljava/lang/Object;Ljava/lang/Object;)I
      #29 invokestatic com/jd/jvm/exec/DynamicInvoke.lambda$main$0:(Ljava/lang/Object;Ljava/lang/Object;)I
      #28 (Ljava/lang/Object;Ljava/lang/Object;)I

*/