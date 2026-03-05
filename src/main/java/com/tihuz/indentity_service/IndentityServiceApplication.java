package com.tihuz.indentity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IndentityServiceApplication {

	public static void main(String[] args) {
        System.out.println("Java version: " + System.getProperty("java.version"));

        SpringApplication.run(IndentityServiceApplication.class, args);
	}

}
