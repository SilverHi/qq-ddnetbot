package com.silver.ddrtools.common.util;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
/**
 * @ClassName SpringUtil
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 15:46
 * @Version 1.0
 **/


@Component
public class SpringUtil {

    private static AutowireCapableBeanFactory beanFactory;

    public static void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        SpringUtil.beanFactory = beanFactory;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }
}
