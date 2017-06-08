package com.wz.web.beanPostProcessors;

import com.alibaba.fastjson.JSON;
import com.wz.web.domain.Demo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by wangzhen on 2016-07-24.
 */
public class DemoPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName + "++++++++++postProcessBeforeInitialization");
        if(bean instanceof Demo){
            Demo user = (Demo)bean;
            user.setUsername("新名字");
            user.setId(100l);
            return user;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName + "-----postProcessAfterInitialization");
        return bean;
    }
}
