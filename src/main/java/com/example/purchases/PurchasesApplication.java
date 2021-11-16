package com.example.purchases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PurchasesApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PurchasesApplication.class);

        String port = context.getEnvironment().getProperty("server.port");
        System.out.println("http://localhost:" + port + "/");
    }
}
