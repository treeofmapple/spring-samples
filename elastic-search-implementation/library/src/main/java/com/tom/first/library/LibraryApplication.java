package com.tom.first.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.first.library.config.CustomBanner;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LibraryApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
