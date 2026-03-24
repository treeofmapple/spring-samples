package com.tom.first.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.first.simple.config.CustomBanner;

@SpringBootApplication
public class SimpleSystemApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SimpleSystemApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
