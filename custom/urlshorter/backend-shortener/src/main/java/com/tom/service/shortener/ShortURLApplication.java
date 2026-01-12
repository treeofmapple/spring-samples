package com.tom.service.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tom.service.shortener.common.CustomBanner;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ShortURLApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ShortURLApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
