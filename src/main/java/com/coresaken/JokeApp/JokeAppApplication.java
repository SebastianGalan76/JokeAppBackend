package com.coresaken.JokeApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JokeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokeAppApplication.class, args);
	}

}
