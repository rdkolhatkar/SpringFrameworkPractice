package com.practice.ratnakar;

import com.practice.ratnakar.servlet.TestServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class ServletApplication {
    public static void main(String[] args) throws LifecycleException {
        // To call the servlet we have to start the Tomcat Server, for that we need to call the Tomcat
        Tomcat tomcat = new Tomcat();
        System.out.println("Calling the Tomcat Server ................");
        // Setting the http Port for tomcat
        tomcat.setPort(8091);
        // Now to do the mapping of our servlet we have to call the context of that class
        Context servletContext = tomcat.addContext("", null); // default configuration of servlet context
        // Calling the TestServlet class
        Tomcat.addServlet(servletContext, "TestServlet", new TestServlet());
        // Now we will do the actual Mapping of Servlet
        // {/greetings/hello} this endpoint is defined in the TestServlet
        servletContext.addServletMappingDecoded("/greetings/hello", "TestServlet");
        // Starting the server
        tomcat.start();
        // By default Tomcat server will stop running after few seconds, once it is invoked
        // To keep server alive use the below code
        tomcat.getServer().await();
    }
}
