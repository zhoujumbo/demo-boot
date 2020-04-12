package com.fortunetree.demo.core.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SpringContextUtil
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/1/7
 * @Version 1.0
 */
@Configuration
public class SpringContextUtil implements BeanFactoryAware {
    private static BeanFactory beanFactory = null;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory =beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static <T> T getInstance(String beanName) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }


    public static <T> T getBean(String beanName) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object getBean(String beanName, Class cla) {
        return beanFactory.getBean(beanName, cla);
    }
}
