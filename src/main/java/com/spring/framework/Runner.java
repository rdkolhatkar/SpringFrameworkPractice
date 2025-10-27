package com.spring.framework;

import com.spring.framework.model.Desktop;
import com.spring.framework.model.Developer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("RunnerApplicationContext.xml");
        Developer dev = (Developer) context.getBean("developer");
        // Now we have to create the Above "Developer" bean without type-casting the (Developer) context.getBean("developer")
        // To avoid the type casting we will use the below syntax
        Developer devP = context.getBean("developer", Developer.class);
        // Now we don't want to specify the ame of the bean, we just want to call that class directly as bean then in that case we will use the following syntax
        // Desktop desktop = context.getBean(Desktop.class); // For this syntax to work we have to define our bean with autowire="byType" or primary="true" in configuration.xml or context.xml
        dev.coding();
        System.out.println(dev.getComputer());
        System.out.println(dev.getDuration());
        System.out.println(dev.age);
    }
}
