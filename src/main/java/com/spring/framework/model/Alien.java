package com.spring.framework.model;

public class Alien {
    public int age;

    private int duration;

    public Alien() {
        System.out.println("Alien Object created ......");
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        System.out.println("Setter is called");
        this.duration = duration;
    }

    public void coding(){
        System.out.println("Coding with Spring Framework .................");
    }
}
