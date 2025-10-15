package com.tom.aws.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class KafkaRecieverApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaRecieverApplication.class, args);
	}

}
