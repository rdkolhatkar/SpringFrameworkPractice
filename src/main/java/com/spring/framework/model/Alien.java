package com.spring.framework.model;

import java.beans.ConstructorProperties;

public class Alien {
    public int age;
    private int duration;
    // Injecting Laptop Object inside Alien class
    private Laptop laptop;
    private int timevalue;
    private int timeScale;

    public Alien() {
        System.out.println("Alien Object created ......");
    }
    /*
    @ConstructorProperties({"laptop", "timevalue", "timeScale"}) maps constructor parameters to bean property names. It helps Spring and other frameworks correctly inject or bind values to these fields, especially when parameter names aren’t available at runtime.
    To use the Constructor Properties you have to use "name" tag inside your SpringApplicationContext.xml
    Check the below example
    ---
    <bean id="alien" class="com.spring.framework.model.Alien">
    <constructor-arg name="laptop" ref="device"/>
    <constructor-arg name="timevalue" value="21"/>
    <constructor-arg name="timeScale" value="42"/>
    </bean>
    ---
    */

    @ConstructorProperties({"laptop", "timevalue", "timeScale"})
    public Alien(Laptop laptop, int timevalue, int timeScale) {
        System.out.println("Assigning the value to this constructor through SpringApplicationContext.xml ");
        this.laptop = laptop;
        this.timevalue = timevalue;
        this.timeScale = timeScale;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        System.out.println("Setter is called");
        this.duration = duration;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public void coding(){
        System.out.println("Coding with Spring Framework .................");
        laptop.compile();
    }

}
