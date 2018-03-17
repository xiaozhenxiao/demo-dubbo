package com.smile.wz.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * wangzhen23
 * 2018/3/15.
 */
public class WZNamespaceHandler  extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new WZBeanDefinitionParser());
        registerBeanDefinitionParser("reference", new WZBeanDefinitionParser());
    }
}
