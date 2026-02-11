package com.tom.mail.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.mail.sender.config.CustomBanner;

@SpringBootApplication
public class SimpleMailSenderApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SimpleMailSenderApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
