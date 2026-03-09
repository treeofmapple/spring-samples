package com.tom.benchmark.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.benchmark.monolith.config.CustomBanner;

@SpringBootApplication
public class MonolithApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MonolithApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
