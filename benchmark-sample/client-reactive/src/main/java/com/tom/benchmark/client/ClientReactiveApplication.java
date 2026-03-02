package com.tom.benchmark.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.benchmark.client.config.CustomBanner;

@SpringBootApplication
public class ClientReactiveApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientReactiveApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
