package com.tom.benchmark.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.benchmark.order.config.CustomBanner;

@SpringBootApplication
public class OrderReactiveApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OrderReactiveApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
