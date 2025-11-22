package com.tom.first.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.first.vehicle.config.CustomBanner;

@SpringBootApplication
public class VehicleApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(VehicleApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
