package com.wz.java.classloader;

import java.io.*;

/**
 * TODO
 * wangzhen23
 * 2017/7/30.
 */
public class ClassReLoader extends ClassLoader{
    private String classpath;
    String classname = "compile.Yufa";

    public ClassReLoader(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getData(name);
        if(classData == null){
            throw new ClassNotFoundException();
        }else {
            return defineClass(classname, classData, 0, classData.length);
        }
    }

    private byte[] getData(String name) {
        String path = classpath + classname;
        try {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int num = 0;
            while ((num = is.read(buffer)) != -1){
                stream.write(buffer, 0, num);
            }
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
