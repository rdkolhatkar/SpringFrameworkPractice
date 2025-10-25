package com.spring.framework.model;

public class Laptop implements Computer {

    public int price;
    public Laptop() {
        System.out.println("Laptop Object created ......");
    }

    public void machine(){
        System.out.println("Laptop is a Machine");
    }

    @Override
    public void compile(){
        System.out.println("Compiling using Laptop >>>>>>>>>>>>>>>>>>");
    }
}
