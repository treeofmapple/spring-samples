package com.tom.rabbitmq.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.rabbitmq.realtime.config.CustomBanner;

@SpringBootApplication
public class RealTimeApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RealTimeApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
