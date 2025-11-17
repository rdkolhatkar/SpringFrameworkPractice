package com.spring.framework.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "employee_details", schema = "employeedata")
@Data   // Lombok annotation: auto-generates getters, setters, toString, equals, hashCode
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;   // Auto-generated unique employee ID

    private String employeeName;
    private String email;
    private String address;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
