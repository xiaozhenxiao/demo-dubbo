package com.wz.web.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * Created by wangzhen23 on 2017/4/13.
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MyPlugin implements Interceptor {
    Properties props = null;

    /**
     * 代替拦截对象方法的内容
     * @param invocation 责任链对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.err.println("before.......");
        Object obj = invocation.proceed();
        System.err.println("afert........");
        return obj;
    }

    /**
     * 生成对象的代理 常用Mybatis提供的Plugin类的wrap方法
     * @param target 被代理的对象
     * @return
     */
    @Override
    public Object plugin(Object target) {
        System.err.println("调用生成代理对象........" + target.getClass().getName());
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.err.println("==================" + properties.get("dbType"));
        this.props = properties;
    }
}
