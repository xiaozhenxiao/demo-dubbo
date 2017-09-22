package com.wz.java.clazz.jdkproxy;

/**
 * Created by wangzhen on 2016-07-24.
 */
public class JdkProxy {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        ObjectIntercepter proxy = new ObjectIntercepter();
        BookFacade bookProxy = (BookFacade) proxy.bind(new BookFacadeImpl());
        System.out.println("********************************BookFacadeImpl*******************************");
        bookProxy.addBook();
        System.out.println("********************************BookFacadeImpl*******************************");


    }
}

interface BookFacade {
    void addBook();
}

class BookFacadeImpl implements BookFacade {

    @Override
    public void addBook() {
        System.out.println("TODO增加图书方法。。。");
    }
}

