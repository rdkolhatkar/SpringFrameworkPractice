package com.spring.framework.config;


import com.spring.framework.model.Desktop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfig {

    // By default our Method name is considered as the name of our bean unless we specify the bean name with @Bean annotation, Like in this case default bean name is "desktop"
    @Bean(name = "Computer") // With this syntax we can specify the name of bean, We can also specify the multiple names with Array
    public Desktop desktop(){
        return new Desktop();
    }

    @Bean(name = {"Comp","Device","Machine"})
    public Desktop desktopComputer(){
        return new Desktop();
    }

}
