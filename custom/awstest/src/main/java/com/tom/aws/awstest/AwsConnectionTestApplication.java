package com.tom.aws.awstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.tom.aws.awstest.config.CustomBanner;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class AwsConnectionTestApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AwsConnectionTestApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
