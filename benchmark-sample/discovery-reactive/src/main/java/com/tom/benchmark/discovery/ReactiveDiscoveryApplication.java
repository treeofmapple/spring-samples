package com.tom.benchmark.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.tom.benchmark.discovery.config.CustomBanner;

@EnableEurekaServer
@SpringBootApplication
public class ReactiveDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReactiveDiscoveryApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
