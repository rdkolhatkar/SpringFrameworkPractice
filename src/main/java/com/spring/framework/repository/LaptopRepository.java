package com.spring.framework.repository;

import com.spring.framework.model.Laptop;
import org.springframework.stereotype.Repository;

@Repository // @Repository annotation in Spring marks a class as a Data Access Object (DAO) that handles database operations and also translates persistence exceptions into Spring’s DataAccessException.
public class LaptopRepository {
    public void save(Laptop lap){
        System.out.println("Saving the Data related to laptop entity inside the Database ====================");
    }
}
