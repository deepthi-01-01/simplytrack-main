package com.simplytrack.simplytrackapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class SimplytrackapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplytrackapiApplication.class, args);

    }


}
