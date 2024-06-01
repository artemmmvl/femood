package com.example.femood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FemoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FemoodApplication.class, args);
    }

}
