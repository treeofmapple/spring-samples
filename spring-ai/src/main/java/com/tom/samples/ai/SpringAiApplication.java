package com.tom.samples.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.samples.ai.config.CustomBanner;

@SpringBootApplication
public class SpringAiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringAiApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

	// My first run test i can make more features but these are for testing now. I like it i can make more stuff. But not for now.
	
}
