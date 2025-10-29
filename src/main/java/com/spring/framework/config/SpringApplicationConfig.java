package com.spring.framework.config;


import com.spring.framework.model.Computer;
import com.spring.framework.model.Desktop;
import com.spring.framework.model.Developer;
import com.spring.framework.model.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringApplicationConfig {

    // By default our Method name is considered as the name of our bean unless we specify the bean name with @Bean annotation, Like in this case default bean name is "desktop"
    @Bean(name = "Computer") // With this syntax we can specify the name of bean, We can also specify the multiple names with Array
    @Primary
    public Desktop desktop(){
        return new Desktop();
    }

    @Bean(name = {"Comp","Device","Machine"})
    @Scope("prototype")
    public Desktop desktopComputer(){
        return new Desktop();
    }

    /*
    @Bean
    public Developer developer(){
        Developer dev = new Developer();
        dev.setAge(22);
        dev.setDuration(2000);
        dev.setTimevalue(123);
        dev.setTimeScale(321);
        dev.setComputer(desktop()); // Passing the object of Desktop
        return dev;
    }
    */
    // There is one more way with which we can pass the object of computer through constructor
    @Bean
    @Primary
    public Developer develop(@Autowired  @Qualifier("Computer") Computer comp){
        Developer dev = new Developer();
        dev.setAge(22);
        dev.setDuration(2000);
        dev.setTimevalue(123);
        dev.setTimeScale(321);
        dev.setComputer(comp); // Passing the object of comp which is defined as constructor parameter
        return dev;
    }

    @Bean
    public Laptop laptop(){
        return new Laptop();
    }

}
