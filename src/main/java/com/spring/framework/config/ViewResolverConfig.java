package com.spring.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class ViewResolverConfig {

    /**
     * -------------------------
     * FIRST VIEW RESOLVER
     * -------------------------
     * This resolver will look for JSP pages under:
     *
     *      /WEB-INF/jsp/*.jsp
     *
     * and it has ORDER = 1, so Spring will try this resolver FIRST.
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver() {

        // Create a new JSP view resolver
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        // Set the directory where JSP files for the main site are located
        resolver.setPrefix("/WEB-INF/jsp/");

        // Set the file extension for all views handled by this resolver
        resolver.setSuffix(".jsp");

        // This resolver will be tried first (lowest number = highest priority)
        resolver.setOrder(1);

        return resolver;
    }


    /**
     * -------------------------
     * SECOND VIEW RESOLVER
     * -------------------------
     * This resolver looks for JSP pages under:
     *
     *      /WEB-INF/view/*.jsp
     *
     * It is used only if the first resolver
     * (jspViewResolver) does NOT find the requested view.
     *
     * ORDER = 2 → Spring checks this resolver SECOND.
     */
    @Bean
    public InternalResourceViewResolver secondViewResolver() {

        // Create another JSP view resolver
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        // Set the second folder containing JSP files for your second website/module
        resolver.setPrefix("/WEB-INF/view/");

        // Set file type for this resolver (can be .jsp or .html depending on your files)
        resolver.setSuffix(".jsp");

        // Spring will check this resolver only if the first one fails
        resolver.setOrder(2);

        return resolver;
    }
}
