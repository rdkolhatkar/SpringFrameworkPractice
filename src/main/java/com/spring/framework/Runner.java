package com.spring.framework;

import com.spring.framework.model.Developer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("RunnerApplicationContext.xml");
        Developer dev = (Developer) context.getBean("developer");
        dev.coding();
        System.out.println(dev.getComputer());
        System.out.println(dev.getDuration());
        System.out.println(dev.age);
    }
}
