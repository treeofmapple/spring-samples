package com.tom.aws.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AwsLambdaSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsLambdaSampleApplication.class, args);
	}

}
