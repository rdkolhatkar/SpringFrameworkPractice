package com.spring.framework.model;

import java.beans.ConstructorProperties;

public class Developer {
    public int age;
    private int duration;
    // Injecting Laptop Object inside Alien class
    private Computer computer;
    private int timevalue;
    private int timeScale;

    public Developer() {
        System.out.println("Alien Object created ......");
    }
    @ConstructorProperties({"age","duration","computer","timevalue","timeScale"})
    public Developer(int age, int duration, Computer computer, int timevalue, int timeScale) {
        this.age = age;
        this.duration = duration;
        this.computer = computer;
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

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public void coding(){
        System.out.println("Coding with Spring Framework .................");
        computer.compile();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(int timevalue) {
        this.timevalue = timevalue;
    }

    public int getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(int timeScale) {
        this.timeScale = timeScale;
    }
}
