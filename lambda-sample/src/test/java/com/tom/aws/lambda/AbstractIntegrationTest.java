package com.tom.aws.lambda;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractIntegrationTest {

	static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.2.0");

	@SuppressWarnings("resource")
	@Container
	public static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
			.withServices(LocalStackContainer.Service.DYNAMODB);

	@DynamicPropertySource
	static void dynamodbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.aws.dynamodb.endpoint",
				() -> localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
		registry.add("spring.cloud.aws.credentials.access-key", localstack::getAccessKey);
		registry.add("spring.cloud.aws.credentials.secret-key", localstack::getSecretKey);
		registry.add("spring.cloud.aws.region.static", localstack::getRegion);
	}
}
