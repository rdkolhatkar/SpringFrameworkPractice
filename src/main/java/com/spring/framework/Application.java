package com.spring.framework;

import com.spring.framework.model.Alien;
import com.spring.framework.model.Laptop;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    // Steps to create the Object with the help of Spring Framework using container
    /*
        1) To create the object with Spring Container we should first import the ApplicationContext
        2) To use ApplicationContext we have to add dependency called as spring-context
        3) Create object "ApplicationContext context = new ClassPathXmlApplicationContext()"
        4) Then use method called .getBean("<bean name>") from BeanFactory
        5) As BeanFactory is implemented by ListableBeanFactory and this ListableBeanFactory is implemented by ApplicationContext, so we can directly call getBean() method
    */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringApplicationContext.xml"); // Passing the xml configuration file
        System.out.println("Prototype Object Creation in Spring Framework");
        // Creating first object of Alien Class
        Alien alien = (Alien) context.getBean("alien");
        System.out.println(alien.age);
        alien.coding();
        // Creating new object of Alien class
        Alien unknown = (Alien) context.getBean("alien");
        unknown.age = 33;
        System.out.println(unknown.age);
        System.out.println("Singleton Object Creation in Spring Framework");
        // Creating first object of Laptop Class
        Laptop laptopOne = (Laptop) context.getBean("device");
        System.out.println(laptopOne.price);
        // Creating second object of Laptop Class
        Laptop laptopTwo = (Laptop) context.getBean("device");
        laptopTwo.price = 200;
        laptopTwo.machine();
        System.out.println(laptopTwo.price);
        // Setter Injection in the Spring Framework is a type of Dependency Injection (DI) where Spring injects dependencies into a bean using its setter methods after the bean is created.
        // In Setter Injection, Spring first calls the no-argument constructor (or uses default instantiation) to create the bean, and then it calls the setter methods to inject the required dependencies.
        Alien alienEntity = (Alien) context.getBean("entity");
        System.out.println("Alien Entity Duration is "+alienEntity.getDuration());
    }

}
