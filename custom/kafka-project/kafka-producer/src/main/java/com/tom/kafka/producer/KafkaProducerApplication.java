package com.tom.kafka.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.tom.kafka.producer.common.CustomBanner;

@EnableAsync
@SpringBootApplication
public class KafkaProducerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KafkaProducerApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
