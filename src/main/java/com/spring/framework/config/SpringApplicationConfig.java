package com.spring.framework.config;


import com.spring.framework.model.Desktop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfig {

    @Bean
    public Desktop desktop(){
        return new Desktop();
    }

}
