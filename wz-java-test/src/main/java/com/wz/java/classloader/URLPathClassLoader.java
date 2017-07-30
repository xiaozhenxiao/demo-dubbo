package com.wz.java.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * TODO
 * wangzhen23
 * 2017/7/30.
 */
public class URLPathClassLoader extends URLClassLoader{
    private String pachageName = "";

    public URLPathClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = findLoadedClass(name);
        if(aClass != null){
            return aClass;
        }
        if(!pachageName.startsWith(name)){
            return super.loadClass(name);
        }else {
            return findClass(name);
        }
    }
}
