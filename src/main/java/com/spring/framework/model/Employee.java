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

    @Column(name = "employee_name")
    private String employeeName;
    private String email;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    @Column(name = "pin_code")
    private String pinCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
}
