package com.tom.kafka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import com.tom.kafka.consumer.common.CustomBanner;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class KafkaRecieverApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KafkaRecieverApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
