package com.tom.reactive.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.reactive.first.config.CustomBanner;

@SpringBootApplication
public class TryingReactiveApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TryingReactiveApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
