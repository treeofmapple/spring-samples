package com.tom.benchmark.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.benchmark.gateway.config.CustomBanner;

@SpringBootApplication
public class ReactiveGatewayApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReactiveGatewayApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
