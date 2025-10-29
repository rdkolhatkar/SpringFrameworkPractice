package com.spring.framework;

import com.spring.framework.config.SpringApplicationConfig;
import com.spring.framework.model.Desktop;
import com.spring.framework.model.Developer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Engine {
    public static void main(String[] args) {
        // If we have to interact with Spring Container through XML then we will use the {ApplicationContext context = new ClassPathXmlApplicationContext("SpringApplicationContext.xml")} this syntax
        // If we have to interact with Spring Container through Java based configuration then we will use the {ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApplicationConfig.class)} this syntax
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        Desktop desktop = applicationContext.getBean("Computer",Desktop.class);
        desktop.compile();
        Desktop desktopOne = applicationContext.getBean("Machine",Desktop.class);
        desktopOne.compile();
        Desktop desktopTwo = applicationContext.getBean(Desktop.class);
        desktopTwo.compile();
        Desktop desktopThree = applicationContext.getBean(Desktop.class);
        desktopThree.compile();

        Developer devP = applicationContext.getBean(Developer.class);
        devP.coding();
        System.out.println(devP.getComputer());
        System.out.println(devP.getDuration());
        System.out.println(devP.age);
    }
}
