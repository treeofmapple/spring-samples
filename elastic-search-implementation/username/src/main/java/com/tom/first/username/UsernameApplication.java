package com.tom.first.username;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.first.username.config.CustomBanner;

@SpringBootApplication
public class UsernameApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UsernameApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
