package com.sumerge.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoursesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursesAppApplication.class, args);
	}

}
