package com.silver.ddrtools.common.util;

/**
 * @ClassName SpringUtilInitializer
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 15:47
 * @Version 1.0
 **/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringUtilInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SpringUtil.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
    }
}
