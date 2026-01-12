package com.tom.service.shortener.common;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class RedisUtils {

	private final RedisConnectionFactory redisConnectionFactory;
	private final RedisTemplate<String, String> redisTemplate;

	public boolean isRedisAvailable() {
		try {
			return redisConnectionFactory.getConnection().ping() != null;
		} catch (RedisConnectionFailureException e) {
			log.warn("Redis connection failed: {}", ZonedDateTime.now());
			return false;
		}
	}

	public void insertRedisData(String dataInsert, String params, boolean expire) {
		isRedisAvailable();
		redisTemplate.opsForValue().set(dataInsert, params);
		if (expire) {
			redisTemplate.expire(dataInsert, Duration.ofMinutes(10));
		}
	}

	public void insertRedisData(String dataInsert, String paramsToSet, long expirationTime) {
		isRedisAvailable();
		redisTemplate.opsForValue().set(dataInsert, paramsToSet);
		Duration duration = expirationTime > 0 ? Duration.ofMinutes(expirationTime) : Duration.ofMinutes(20);
		redisTemplate.expire(dataInsert, duration);
	}

	public void insertRedisDataIncrement(String dataInsert, String params, String params2, boolean dataExpiration) {
		isRedisAvailable();
		redisTemplate.opsForValue().increment(dataInsert);
		redisTemplate.opsForValue().set(params, params2);
		if (dataExpiration) {
			redisTemplate.expire(dataInsert, Duration.ofMinutes(20));
		}
	}

	public void insertRedisDataIncrement(String dataInsert, String params, String params2, Duration expirationTime) {
		isRedisAvailable();
		redisTemplate.opsForValue().increment(dataInsert);
		redisTemplate.opsForValue().set(params, params2);
		redisTemplate.expire(dataInsert, expirationTime);
	}

	public String retrieveRedisData(String dataRetrieve) {
		return redisTemplate.opsForValue().get(dataRetrieve);
	}

	public Cursor<String> scanRedisData(String query, int quantity) {
		return redisTemplate.scan(ScanOptions.scanOptions().match(query).count(quantity).build());
	}

	/*
	 * make a way to if the connection get cut
	 * it run again to delete the data that wasn't deleted 
	 */
	
	public void removeKey(String keyToRemove) {
		isRedisAvailable();
		redisTemplate.delete(keyToRemove);
	}

}
