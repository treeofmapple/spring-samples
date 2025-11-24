package com.tom.awstest.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.awstest.lambda.config.CustomBanner;

@SpringBootApplication
public class LambdaImplementationApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LambdaImplementationApplication.class);
		//app.setWebApplicationType(WebApplicationType.SERVLET);
		app.setBanner(new CustomBanner());
		app.run();
	}

}
 