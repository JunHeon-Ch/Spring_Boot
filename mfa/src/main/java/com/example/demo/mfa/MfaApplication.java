package com.example.demo.mfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.demo.mfa"})
public class MfaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfaApplication.class, args);
    }

}
