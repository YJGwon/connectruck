package com.connectruck.foodtruck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FoodtruckApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodtruckApplication.class, args);
    }
}
