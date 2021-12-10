package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)         //启动基于注解的权限控制
public class NewSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewSecurityApplication.class, args);
    }

}
