package com.practice.ratnakar;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class ServletApplication {
    public static void main(String[] args) throws LifecycleException {
        // To call the servlet we have to start the Tomcat Server, for that we need to call the Tomcat
        System.out.println("Calling the Tomcat Server ................");
        Tomcat tomcat = new Tomcat();
        // Starting the server
        tomcat.start();
        // By default Tomcat server will stop running after few seconds, once it is invoked
        // To keep server alive use the below code
        tomcat.getServer().await();
    }
}
