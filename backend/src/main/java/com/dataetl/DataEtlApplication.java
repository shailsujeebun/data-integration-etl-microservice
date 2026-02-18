package com.dataetl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataEtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataEtlApplication.class, args);
    }
}
