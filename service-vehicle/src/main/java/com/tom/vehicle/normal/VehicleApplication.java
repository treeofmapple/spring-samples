package com.tom.vehicle.normal;

import com.tom.vehicle.normal.config.CustomBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehicleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VehicleApplication.class);
        app.setBanner(new CustomBanner());
        app.run(args);
    }
}
