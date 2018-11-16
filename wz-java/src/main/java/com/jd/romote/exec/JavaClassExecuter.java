package com.jd.romote.exec;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * JavaClass执行工具
 *
 * @author zzm
 */
public class JavaClassExecuter {

    /**
     * 执行外部传过来的代表一个Java类的Byte数组<br>
     * 将输入类的byte数组中代表java.lang.System的CONSTANT_Utf8_info常量修改为劫持后的HackSystem类
     * 执行方法为该类的static main(String[] args)方法，输出结果为该类向System.out/err输出的信息
     * @param classAllName class文件的全路径名
     * @return 执行结果
     */
    public static String execute(String classAllName) {
        FileInputStream is = null;
        //代表一个Java类的Byte数组
        byte[] classByte;
        try {
            is = new FileInputStream(classAllName);
            classByte = new byte[is.available()];
            is.read(classByte);

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }
        }
        HackSystem.clearBuffer();
        ClassModifier cm = new ClassModifier(classByte);
        byte[] modiBytes = cm.modifyUTF8Constant("java/lang/System", "com/jd/romote/exec/HackSystem");
        HotSwapClassLoader loader = new HotSwapClassLoader();
        Class clazz = loader.loadByte(modiBytes);
        try {
            Method method = clazz.getMethod("main", new Class[] { String[].class });
            method.invoke(null, new String[] { null });
        } catch (Throwable e) {
            e.printStackTrace(HackSystem.out);
        }
        return HackSystem.getBufferString();
    }
}