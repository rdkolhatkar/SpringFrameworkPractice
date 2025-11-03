package com.spring.framework.service;

import com.spring.framework.model.Laptop;
import org.springframework.stereotype.Service;

@Service // The @Service annotation in Spring marks a class as a service component, indicating it holds business logic and should be managed as a Spring bean.
public class LaptopService {
    public void addLaptop(Laptop lap){
       System.out.println("Method Called ----------------");
    }
    public boolean isGoodForProgramming(Laptop lap){
        return true;
    }
}
