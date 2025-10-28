package com.spring.framework;

import com.spring.framework.config.SpringApplicationConfig;
import com.spring.framework.model.Desktop;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Engine {
    public static void main(String[] args) {
        // If we have to interact with Spring Container through XML then we will use the {ApplicationContext context = new ClassPathXmlApplicationContext("SpringApplicationContext.xml")} this syntax
        // If we have to interact with Spring Container through Java based configuration then we will use the {ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApplicationConfig.class)} this syntax
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        Desktop desktop = applicationContext.getBean(Desktop.class);
        desktop.compile();
    }
}
