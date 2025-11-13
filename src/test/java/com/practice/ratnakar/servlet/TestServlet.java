package com.practice.ratnakar.servlet;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/greetings/hello")
public class TestServlet extends HttpServlet {

    // Servlet is basically used when we have to interact with FrontEnd User Interface client and backend server client
    // Here we are receiving data from front end UI/Mobile App in the form of HTTP request & We are sending that data after some intermediate processing to the backend server
    // Then we are returning the server response and sending the response back to the front end through HTTP response
    // For above use case we are using the two interfaces from "jakarta.servlet" called HttpServletRequest & HttpServletResponse
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("Initializing the Servlet .....................");
        // Setting the type of data which is getting published in response object of servlet
        res.setContentType("text/html");
        // Sending response to the Web Client or Mobile Client
        PrintWriter out = res.getWriter(); // Writing some content on response object with getWriter() method
        out.println("<h2><b>Hello World !</b></h2>"); // Writing our Message with HTML Tags for this we have to set the type of data for our web client in the response object
    }
}
