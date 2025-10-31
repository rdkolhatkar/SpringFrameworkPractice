package com.spring.framework.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // @Scope defines the lifecycle of a Spring bean, like singleton (one instance) or prototype (new instance per request), controlling how and when Spring creates and manages bean objects.
public class Desktop implements Computer{
    public Desktop() {
        System.out.println("Desktop object is created ------");
    }

    @Override
    public void compile() {
        System.out.println("Compiling using Desktop -----------------------------");
    }
}
