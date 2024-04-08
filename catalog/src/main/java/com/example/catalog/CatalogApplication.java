package com.example.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.catalog")

public class CatalogApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(CatalogApplication.class, args);
	}

}
