package com.tom.tools.dynamodb;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Profile("test")
@Component
public class DynamoDbUsageApplicationTests {

	private String endpoint;

	@SuppressWarnings("resource")
	private final GenericContainer<?> dynamodb = new GenericContainer<>("amazon/dynamodb-local:latest")
			.withExposedPorts(8000);

	@PostConstruct
	public void startDynamoDbLocal() {
		dynamodb.start();
		endpoint = "http://localhost:" + dynamodb.getMappedPort(8000);
		System.setProperty("dynamodb.endpoint", endpoint);
		System.out.println("DynamoDB Local running on " + endpoint);
	}

	@PreDestroy
	public void stopDynamoDb() {
		dynamodb.stop();
	}

}
