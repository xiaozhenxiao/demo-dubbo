package com.smile.wz.spring.schema;

import com.smile.wz.spring.ReferenceBean;
import com.smile.wz.spring.ServiceBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * wangzhen23
 * 2018/3/15.
 */
public class WZBeanDefinitionParser implements BeanDefinitionParser {
    private final Class<?> beanClass;

    public WZBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    protected BeanDefinition doParse(Element element, ParserContext parserContext, Class<?> beanClass) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String id = element.getAttribute("id");
        if (id == null || id.length() == 0) {
            String generatedBeanName = element.getAttribute("name");
            if (generatedBeanName == null || generatedBeanName.length() == 0) {
                generatedBeanName = element.getAttribute("interface");
            }
            if (generatedBeanName == null || generatedBeanName.length() == 0) {
                generatedBeanName = beanClass.getName();
            }
            id = generatedBeanName;
            int counter = 2;
            while (parserContext.getRegistry().containsBeanDefinition(id)) {
                id = generatedBeanName + (counter++);
            }
        }
        if (id != null && id.length() > 0) {
            if (parserContext.getRegistry().containsBeanDefinition(id)) {
                throw new IllegalStateException("Duplicate spring bean id " + id);
            }
            parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
            beanDefinition.getPropertyValues().addPropertyValue("id", id);
        }

        if (ServiceBean.class.equals(this.beanClass)) {

            String interfaceVal = element.getAttribute("interface");
            beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceVal);
            String refVal = element.getAttribute("ref");
            if (parserContext.getRegistry().containsBeanDefinition(refVal)) {
                BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(refVal);
                if (!refBean.isSingleton()) {
                    throw new IllegalStateException("The exported service ref " + refVal + " must be singleton! Please set the " + refVal + " bean scope to singleton, eg: <bean id=\"" + refVal + "\" scope=\"singleton\" ...>");
                }
            }
            Object reference = new RuntimeBeanReference(refVal);
            beanDefinition.getPropertyValues().addPropertyValue("ref", reference);

            String port = element.getAttribute("port");
            beanDefinition.getPropertyValues().addPropertyValue("port", Integer.valueOf(port));

        } else if (ReferenceBean.class.equals(this.beanClass)) {
            String interfaceVal = element.getAttribute("interface");
            beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceVal);

            String address = element.getAttribute("address");
            beanDefinition.getPropertyValues().addPropertyValue("address", address);

            String async = element.getAttribute("async");
            beanDefinition.getPropertyValues().addPropertyValue("async", Boolean.valueOf(async));
        }
        return beanDefinition;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return doParse(element, parserContext, beanClass);
    }
}
