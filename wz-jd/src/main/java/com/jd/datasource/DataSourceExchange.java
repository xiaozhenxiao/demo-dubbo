package com.jd.datasource;

import com.jd.annotation.DataSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Component
class DataSourceExchange implements MethodInterceptor {

//    private Logger logger = LoggerFactory.getLogger(DataSourceExchange.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Method name : " + invocation.getMethod().getName());
        System.out.println("Method arguments : " + Arrays.toString(invocation.getArguments()));
        DataSource dataSource = this.getDataSource(invocation);
        if (dataSource == null) {
//            logger.error("dataSource in invocation is null");
            DataSourceTypeManager.setDataSourceType(DataSourceType.MASTER);
            System.out.println(Thread.currentThread().getName() + " - " + invocation.getMethod().getName() + " user database is MASTER");
            return invocation.proceed();
        }
        String dbname = dataSource.name();
        Object result = null;
        try {
            DataSourceTypeManager.setDataSourceType(DataSourceType.valueOf(dbname));
            System.out.println(Thread.currentThread().getName() + " - " + invocation.getMethod().getName() + " user database is " + dbname);
            result = invocation.proceed();
        }catch (Exception e){
//            logger.error("change datasource error!", e);
        }finally {
            DataSourceTypeManager.setDataSourceType(DataSourceType.MASTER);
        }
        return result;
    }

    private DataSource getDataSource(MethodInvocation invocation) throws Throwable {
        DataSource dataSource = AnnotationUtils.findAnnotation(invocation.getMethod(), DataSource.class);
        if(dataSource != null) {
            return dataSource; // if use CGlib proxy
        }

        Method proxyedMethod = invocation.getMethod(); // or use jdk proxy
        Method realMethod = invocation.getThis().getClass().getDeclaredMethod(proxyedMethod.getName(), proxyedMethod.getParameterTypes());
        dataSource =  AnnotationUtils.findAnnotation(realMethod, DataSource.class);
        return dataSource;
    }
}
