package com.projectcloud.p_coffeeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PCoffeeshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(PCoffeeshopApplication.class, args);
    }

}
