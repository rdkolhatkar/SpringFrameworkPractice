package com.ratnakar.web.repository;

import com.ratnakar.web.model.StudentsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentListRepository extends JpaRepository<StudentsList, Integer> {
}
