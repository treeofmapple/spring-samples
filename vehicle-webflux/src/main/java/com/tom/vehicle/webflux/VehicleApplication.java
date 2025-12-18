package com.tom.vehicle.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.vehicle.webflux.config.CustomBanner;

@SpringBootApplication
public class VehicleApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(VehicleApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
