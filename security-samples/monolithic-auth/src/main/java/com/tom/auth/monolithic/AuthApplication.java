package com.tom.auth.monolithic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.tom.auth.monolithic.common.CustomBanner;

@EnableCaching
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AuthApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

	// future update
	
	// Oauth2
	
	// Mail Authentication
	
	/* 
	 * instead of deleting the user from the database, 
	 * revoke its access to the system 
	 */ 
	
}
