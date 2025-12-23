package com.ratnakar.web.service;

import com.ratnakar.web.model.StudentsList;
import com.ratnakar.web.repository.StudentListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class StudentDataService {
    @Autowired
    private StudentListRepository studentListRepository;
    public void loadStudents(){
        List<StudentsList> studentsList = new ArrayList<>(Arrays.asList(
                // 1
                new StudentsList(
                        1,
                        "Amit",
                        "Sharma",
                        new Date(99, 4, 12),   // yyyy-MM-dd → 1999-05-12
                        "MG Road, Pune",
                        "amit.sharma@gmail.com",
                        "India",
                        "Maharashtra",
                        "411001"
                ),

                // 2
                new StudentsList(
                        2,
                        "Neha",
                        "Verma",
                        new Date(98, 7, 23),
                        "Sector 18, Noida",
                        "neha.verma@gmail.com",
                        "India",
                        "Uttar Pradesh",
                        "201301"
                ),

                // 3
                new StudentsList(
                        3,
                        "Rahul",
                        "Patil",
                        new Date(100, 1, 5),
                        "Baner Road, Pune",
                        "rahul.patil@gmail.com",
                        "India",
                        "Maharashtra",
                        "411045"
                ),

                // 4
                new StudentsList(
                        4,
                        "Sneha",
                        "Kulkarni",
                        new Date(97, 10, 30),
                        "Kothrud, Pune",
                        "sneha.k@gmail.com",
                        "India",
                        "Maharashtra",
                        "411038"
                ),

                // 5
                new StudentsList(
                        5,
                        "Rohit",
                        "Mehta",
                        new Date(96, 3, 18),
                        "Vastrapur, Ahmedabad",
                        "rohit.mehta@gmail.com",
                        "India",
                        "Gujarat",
                        "380015"
                ),

                // 6
                new StudentsList(
                        6,
                        "Priya",
                        "Nair",
                        new Date(101, 6, 9),
                        "Technopark, Trivandrum",
                        "priya.nair@gmail.com",
                        "India",
                        "Kerala",
                        "695581"
                ),

                // 7
                new StudentsList(
                        7,
                        "Ankit",
                        "Singh",
                        new Date(99, 11, 2),
                        "Indira Nagar, Lucknow",
                        "ankit.singh@gmail.com",
                        "India",
                        "Uttar Pradesh",
                        "226016"
                ),

                // 8
                new StudentsList(
                        8,
                        "Pooja",
                        "Deshmukh",
                        new Date(100, 8, 14),
                        "Hinjewadi, Pune",
                        "pooja.d@gmail.com",
                        "India",
                        "Maharashtra",
                        "411057"
                ),

                // 9
                new StudentsList(
                        9,
                        "Karan",
                        "Malhotra",
                        new Date(98, 2, 27),
                        "Rajouri Garden, Delhi",
                        "karan.m@gmail.com",
                        "India",
                        "Delhi",
                        "110027"
                ),

                // 10
                new StudentsList(
                        10,
                        "Shreya",
                        "Iyer",
                        new Date(101, 0, 19),
                        "Adyar, Chennai",
                        "shreya.iyer@gmail.com",
                        "India",
                        "Tamil Nadu",
                        "600020"
                )
        ));

        //studentListRepository.saveAll(studentsList);

    }
}
