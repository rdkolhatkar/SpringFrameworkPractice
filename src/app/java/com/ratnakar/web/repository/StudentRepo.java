package com.ratnakar.web.repository;

import com.ratnakar.web.model.StudentsData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<StudentsData, Integer> {
    /*
    In this "JpaRepository<StudentData, Integer>" interface first parameter is Which class type you are working with in our case it is StudentData
    And second parameter is database primary key, As we know every DB table needs primary key, So in our case StudentData class has "id" which is primary key
    And the return type of our primary key inside the StudentRepo is int that's why second argument is Integer inside the "JpaRepository<StudentData, Integer>"
    */
}
