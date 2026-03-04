package com.tom.benchmark.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.benchmark.product.config.CustomBanner;

@SpringBootApplication
public class ProductReactiveApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProductReactiveApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
