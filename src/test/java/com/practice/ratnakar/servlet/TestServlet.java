package com.practice.ratnakar.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

    // Servlet is basically used when we have to interact with FrontEnd User Interface client and backend server client
    // Here we are receiving data from front end UI/Mobile App in the form of HTTP request & We are sending that data after some intermediate processing to the backend server
    // Then we are returning the server response and sending the response back to the front end through HTTP response
    // For above use case we are using the two interfaces from "jakarta.servlet" called HttpServletRequest & HttpServletResponse
    public void service(HttpServletRequest req, HttpServletResponse res){

    }
}
