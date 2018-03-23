package com.smile.wz.spring.schema;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.smile.wz.spring.MethodConfig;
import com.smile.wz.spring.ReferenceBean;
import com.smile.wz.spring.ServiceBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * wangzhen23
 * 2018/3/15.
 */
public class WZBeanDefinitionParser implements BeanDefinitionParser {
    private final Class<?> beanClass;
    private final boolean required;

    public WZBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }

    protected BeanDefinition doParse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String id = element.getAttribute("id");
        if ((id == null || id.length() == 0) && required) {
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

        if (ServiceBean.class.equals(beanClass)) {
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
            if(StringUtils.isNotEmpty(port)) {
                beanDefinition.getPropertyValues().addPropertyValue("port", Integer.valueOf(port));
            }

        } else if (ReferenceBean.class.equals(beanClass)) {
            String interfaceVal = element.getAttribute("interface");
            beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceVal);

            /*String address = element.getAttribute("address");
            beanDefinition.getPropertyValues().addPropertyValue("address", address);

            String async = element.getAttribute("async");
            beanDefinition.getPropertyValues().addPropertyValue("async", Boolean.valueOf(async));*/
        }
        Set<String> props = new HashSet<String>();
        for (Method setter : beanClass.getMethods()) {
            String name = setter.getName();
            if (name.length() > 3 && name.startsWith("set")
                    && Modifier.isPublic(setter.getModifiers())
                    && setter.getParameterTypes().length == 1) {
                Class<?> type = setter.getParameterTypes()[0];
                String property = StringUtils.camelToSplitName(name.substring(3, 4).toLowerCase() + name.substring(4), "-");
                props.add(property);
                Method getter = null;
                try {
                    getter = beanClass.getMethod("get" + name.substring(3), new Class<?>[0]);
                } catch (NoSuchMethodException e) {
                    try {
                        getter = beanClass.getMethod("is" + name.substring(3), new Class<?>[0]);
                    } catch (NoSuchMethodException e2) {
                    }
                }
                if (getter == null
                        || !Modifier.isPublic(getter.getModifiers())
                        || !type.equals(getter.getReturnType())) {
                    continue;
                }
                if ("methods".equals(property)) {
                    parseMethods(id, element.getChildNodes(), beanDefinition, parserContext);
                }  else {
                    String value = element.getAttribute(property);
                    if (value != null) {
                        value = value.trim();
                        if (value.length() > 0) {
                            Object reference = null;
                            if (isPrimitive(type)) {
                                if ("async".equals(property) && "false".equals(value)
                                        || "timeout".equals(property) && "0".equals(value)
                                        || "delay".equals(property) && "0".equals(value)
                                        || "version".equals(property) && "0.0.0".equals(value)
                                        || "stat".equals(property) && "-1".equals(value)
                                        || "reliable".equals(property) && "false".equals(value)) {
                                    // backward compatibility for the default value in old version's xsd
                                    value = null;
                                }
                                reference = value;
                            } else if ("onreturn".equals(property)) {
                                int index = value.lastIndexOf(".");
                                String returnRef = value.substring(0, index);
                                String returnMethod = value.substring(index + 1);
                                reference = new RuntimeBeanReference(returnRef);
                                beanDefinition.getPropertyValues().addPropertyValue("onreturnMethod", returnMethod);
                            } else if ("onthrow".equals(property)) {
                                int index = value.lastIndexOf(".");
                                String throwRef = value.substring(0, index);
                                String throwMethod = value.substring(index + 1);
                                reference = new RuntimeBeanReference(throwRef);
                                beanDefinition.getPropertyValues().addPropertyValue("onthrowMethod", throwMethod);
                            } else if ("oninvoke".equals(property)) {
                                int index = value.lastIndexOf(".");
                                String invokeRef = value.substring(0, index);
                                String invokeRefMethod = value.substring(index + 1);
                                reference = new RuntimeBeanReference(invokeRef);
                                beanDefinition.getPropertyValues().addPropertyValue("oninvokeMethod", invokeRefMethod);
                            }
                            if(reference != null) {
                                beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                            }
                        }
                    }
                }
            }
        }
        return beanDefinition;
    }

    private void parseMethods(String id, NodeList nodeList, RootBeanDefinition beanDefinition,
                                     ParserContext parserContext) {
        if (nodeList != null && nodeList.getLength() > 0) {
            ManagedList methods = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if ("method".equals(node.getNodeName()) || "method".equals(node.getLocalName())) {
                        String methodName = element.getAttribute("name");
                        if (methodName == null || methodName.length() == 0) {
                            throw new IllegalStateException("<wz:method> name attribute == null");
                        }
                        if (methods == null) {
                            methods = new ManagedList();
                        }
                        BeanDefinition methodBeanDefinition = doParse(((Element) node), parserContext, MethodConfig.class, false);
                        String name = id + "." + methodName;
                        BeanDefinitionHolder methodBeanDefinitionHolder = new BeanDefinitionHolder(
                                methodBeanDefinition, name);
                        methods.add(methodBeanDefinitionHolder);
                    }
                }
            }
            if (methods != null) {
                beanDefinition.getPropertyValues().addPropertyValue("methods", methods);
            }
        }
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return doParse(element, parserContext, beanClass, required);
    }

    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Byte.class
                || cls == Character.class || cls == Short.class || cls == Integer.class
                || cls == Long.class || cls == Float.class || cls == Double.class
                || cls == String.class || cls == Date.class || cls == Class.class;
    }
}
