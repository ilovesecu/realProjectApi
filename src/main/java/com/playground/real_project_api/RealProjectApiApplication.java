package com.playground.real_project_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RealProjectApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealProjectApiApplication.class, args);
    }

}
