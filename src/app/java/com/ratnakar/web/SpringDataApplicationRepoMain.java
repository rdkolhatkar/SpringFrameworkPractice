package com.ratnakar.web;

import com.ratnakar.web.model.StudentsData;
import com.ratnakar.web.repository.StudentRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class SpringDataApplicationRepoMain {
    public static void main(String[] args) throws ParseException {
        ApplicationContext context = SpringApplication.run(SpringDataApplicationRepoMain.class, args);
        StudentRepo repo = context.getBean(StudentRepo.class);
        StudentsData s1 = new StudentsData();
        StudentsData s2 = new StudentsData();
        StudentsData s3 = new StudentsData();
        // Code for setting the Date of Birth
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Student 1
        s1.setId(101);
        s1.setFirstName("Rahul");
        s1.setLastName("Shetty");
        s1.setAddress("32 Bit Lane Maan road");
        s1.setCountry("India");
        s1.setDob(sdf.parse("1998-07-21"));
        s1.setState("Chennai");
        s1.setPostalCode("12345");
        // Student 2
        s2.setId(102);
        s2.setFirstName("Anita");
        s2.setLastName("Sharma");
        s2.setAddress("221 MG Road, Indiranagar");
        s2.setCountry("India");
        s2.setDob(sdf.parse("1996-03-15"));
        s2.setState("Karnataka");
        s2.setPostalCode("560038");
        // Student 3
        s3.setId(103);
        s3.setFirstName("Vikram");
        s3.setLastName("Patel");
        s3.setAddress("18 River View Society, Satellite");
        s3.setCountry("India");
        s3.setDob(sdf.parse("1994-11-02"));
        s3.setState("Gujarat");
        s3.setPostalCode("380015");
        // Saving the data in DB
        repo.save(s1);


    }
}
