package com.tom.front.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BasicApiProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApiProviderApplication.class, args);
	}

}
