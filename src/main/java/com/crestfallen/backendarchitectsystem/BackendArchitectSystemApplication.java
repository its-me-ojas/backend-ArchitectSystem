package com.crestfallen.backendarchitectsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendArchitectSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendArchitectSystemApplication.class, args);
	}

}
