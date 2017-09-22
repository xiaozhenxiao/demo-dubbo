package com.wz.java.clazz;

import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wangzhen on 2016-07-24.
 */
public class JdkProxy {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        ObjectProxy proxy = new ObjectProxy();
        BookFacade bookProxy = (BookFacade) proxy.bind(new BookFacadeImpl());
        System.out.println("********************************BookFacadeImpl*******************************");
        bookProxy.addBook();
        System.out.println("********************************BookFacadeImpl*******************************");
        BookFacade people = (BookFacade) proxy.bind(new People());
        System.out.println("********************************People*******************************");
        people.addBook();
        System.out.println("********************************People*******************************");

        Object object = Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class[] { BookFacade.class }, new ObjectIntercepter());
        System.out.println("********************************Object*******************************");
        System.out.println("====hashCode:" + object.hashCode());
        System.out.println("====toString:" + object.toString());
        System.out.println("********************************Object*******************************");

        BookFacade peopleSelf = (BookFacade) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class[] { BookFacade.class }, new ObjectIntercepter());
        System.out.println("********************************peopleSelf*******************************");
        peopleSelf.addBook();
        System.out.println("********************************peopleSelf*******************************");

    }
}

interface BookFacade {
    void addBook();
}

class BookFacadeImpl implements BookFacade {

    @Override
    public void addBook() {
        System.out.println("增加图书方法。。。");
    }
}
class People implements BookFacade{
    @Override
    public void addBook() {
        System.out.println("人的方法。。。");
    }
}
class ObjectProxy implements InvocationHandler {
    private Object target;
    /**
     * 绑定委托对象并返回一个代理类
     * @param target
     * @return
     */
    public Object bind(Object target) {
        this.target = target;
        //取得代理对象
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);   //要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
    }

    /**
     * 调用方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        System.out.println("事物开始");
        //执行方法 target
        Object result = method.invoke(target, args);
        System.out.println("事物结束");
        return result;
    }

}

class ObjectIntercepter implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理class:" + method.getDeclaringClass());
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }
        //执行方法
        return method.invoke(new People(), args);
    }
}