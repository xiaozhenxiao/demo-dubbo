package com.wz.web.beanPostProcessors;

import com.wz.web.domain.Demo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by wangzhen on 2016-07-24.
 */
public class DemoPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
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
        return bean;
    }
}
