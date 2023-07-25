package com.cbq.quickbot.spring;

import com.cbq.quickbot.annotation.BotListen;
import com.cbq.quickbot.annotation.GroupListen;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class DefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {



    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        //自定义要扫描的注解 -- 自定义注解
        scanner.addIncludeFilter(new AnnotationTypeFilter(BotListen.class));
        // 自定义扫描的包
        scanner.scan("com");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
