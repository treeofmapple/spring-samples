package com.tom.security.hash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.security.hash.config.CustomBanner;

@SpringBootApplication
public class SecurityHashApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SecurityHashApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
