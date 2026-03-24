package com.tom.vehicle.dynamodb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tom.vehicle.dynamodb.model.Vehicle;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

@Configuration
public class DynamoDbConfig {

	@Value("${dynamodb.region}")
	private String region;

	@Value("${dynamodb.endpoint:}")
	private String endpoint;

	@Value("${dynamodb.use-local:false}")
	private boolean useLocal;

	@Bean
	DynamoDbClient dynamoDbClient() {
		DynamoDbClientBuilder builder = DynamoDbClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create()).region(Region.of(region));

		if (useLocal && endpoint != null && !endpoint.isEmpty()) {
			builder.endpointOverride(URI.create(endpoint));
		}

		return builder.build();
	}

	@Bean
	DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
	}

	@Bean
	DynamoDbTable<Vehicle> vehicleTable(DynamoDbEnhancedClient enhancedClient) {
		DynamoDbTable<Vehicle> table = enhancedClient.table("vehicle", TableSchema.fromBean(Vehicle.class));

		if (useLocal) {
			try {
				table.createTable();
				System.out.println("Local DynamoDB table 'vehicle' created successfully.");
			} catch (ResourceInUseException e) {
				System.out.println("Local DynamoDB table 'vehicle' already exists.");
			}
		}

		return table;
	}

}
