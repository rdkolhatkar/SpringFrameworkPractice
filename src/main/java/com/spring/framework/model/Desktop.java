package com.spring.framework.model;

public class Desktop implements Computer{
    @Override
    public void compile() {
        System.out.println("Compiling using Desktop -----------------------------");
    }
}
