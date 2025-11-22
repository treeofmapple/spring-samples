package com.tom.first.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.first.elastic.config.CustomBanner;

@SpringBootApplication
public class ElasticSearchApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ElasticSearchApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
