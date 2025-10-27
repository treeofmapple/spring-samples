package com.tom.tools.dynamodb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
public class DynamoDbConfig {

	@Value("${dynamodb.endpoint:}")
	private String dynamoDbEndpoint;

	@Value("${dynamodb.region:us-east-1}")
	private String region;

	@Value("${dynamodb.key.access:fakeAccess}")
	private String accessKey;

	@Value("${dynamodb.key.secret:fakeSecret}")
	private String secretKey;

	@Bean
	DynamoDbClient buildDb() {
		return buildAmazonDynamoDB();
	}

	@Bean
	DynamoDbEnhancedClient buildEnhancedClient(DynamoDbClient dynamoDbClient) {
		return dynamoDbEnhancedClient(dynamoDbClient);
	}

	private DynamoDbClient buildAmazonDynamoDB() {
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
				.create(AwsBasicCredentials.create(accessKey, secretKey));

		DynamoDbClientBuilder builder = DynamoDbClient.builder().credentialsProvider(credentialsProvider)
				.region(Region.of(region));

		if (dynamoDbEndpoint != null && !dynamoDbEndpoint.isBlank()) {
			builder.endpointOverride(URI.create(dynamoDbEndpoint));
		}

		return builder.build();
	}

	DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
	}

}
