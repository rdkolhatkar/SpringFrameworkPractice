package com.ratnakar.web.repository;

import com.ratnakar.web.model.StudentsData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentsData, Integer> {
    /*
    In this "JpaRepository<StudentData, Integer>" interface first parameter is Which class type you are working with in our case it is StudentData
    And second parameter is database primary key, As we know every DB table needs primary key, So in our case StudentData class has "id" which is primary key
    And the return type of our primary key inside the StudentRepo is int that's why second argument is Integer inside the "JpaRepository<StudentData, Integer>"
    */

    // In Spring JPA, if we have to write the SQL Query then we use the JPQL language to write DB query to fetch the records
    // It is similar to SQL query but the Only Difference is, in SQL we use tableName, columName and DatabaseName or SchemaName
    // But in JPQL we use the Name of model or PoJo class and the property names
    @Query("select a from StudentsData a where a.firstName = ?1 and a.lastName = ?2")
    List<StudentsData> findByFirstAndLastName(String firstName, String lastName);


}
