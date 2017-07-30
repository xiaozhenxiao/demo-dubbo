package com.wz.java.classloader;

import java.io.*;

/**
 * 加载自定义路径下的class文件
 * wangzhen23
 * 2017/7/30.
 */
public class PathClassLoader extends ClassLoader{
    private String classPath;
    private String packageName;

    public PathClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (packageName.startsWith(name)){
            byte[] classData = getData(name);
            if(classData == null){
                throw new ClassNotFoundException();
            }else {
                return defineClass(name, classData, 0, classData.length);
            }
        }else {
            return super.loadClass(name);
        }
    }

    private byte[] getData(String className) {
        String path = classPath + File.separatorChar + className.replace(',', File.separatorChar) + ".class";
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
