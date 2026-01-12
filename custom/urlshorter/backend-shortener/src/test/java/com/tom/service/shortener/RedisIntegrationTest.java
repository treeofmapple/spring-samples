package com.tom.service.shortener;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.tom.service.shortener.common.RedisUtils;

@DataRedisTest
@Import(RedisUtils.class)
@TestMethodOrder(OrderAnnotation.class)
public class RedisIntegrationTest {

	@Value("${test.redis.port:6379}")
	private static int redisTestPort = 6379;

	private static final String TEST_KEY = "Some";
	private static final String TEST_VALUE = "data";

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@SuppressWarnings("resource")
	@Container
	@ServiceConnection
	static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
	        .withExposedPorts(redisTestPort);

	@Test
	@Order(1)
	void testConnection() {
		boolean availability = redisUtils.isRedisAvailable();
		Assertions.assertThat(availability).isTrue();
	}

	@Test
	@Order(2)
	void testInsert() {
		redisUtils.insertRedisData(TEST_KEY, TEST_VALUE, true);
		String stored = redisTemplate.opsForValue().get(TEST_KEY);
		Assertions.assertThat(stored).isEqualTo(TEST_VALUE);
	}

	@Test
	@Order(3)
	void testDataRetrieval() {
		String retrieved = redisUtils.retrieveRedisData(TEST_KEY);
		Assertions.assertThat(retrieved).isEqualTo(TEST_VALUE);
	}

	@Test
	@Order(4)
	void testDataRemoval() {
		redisUtils.removeKey(TEST_KEY);
		String removed = redisTemplate.opsForValue().get(TEST_KEY);
		Assertions.assertThat(removed).isNull();
	}

}
