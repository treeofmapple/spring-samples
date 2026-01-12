package com.tom.service.shortener.scheduled;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.tom.service.shortener.common.DurationUtils;
import com.tom.service.shortener.repository.URLRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class RedisScheduled {

	@Value("${application.scheduled.redis.connection-check:5M}")
	private String connectionTime;

	@Value("${application.scheduled.redis.access-count:10M}")
	private String syncToDatabase;

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisConnectionFactory redisConnectionFactory;
	private final TaskScheduler scheduler;
	private final URLRepository repository;

	@PostConstruct
	public void checkConnectionAtStartup() {
		Duration checkConnectionTime = DurationUtils.parseDuration(connectionTime);
		Duration syncToDatabaseTime = DurationUtils.parseDuration(syncToDatabase);

		scheduler.scheduleAtFixedRate(this::checkConnection, checkConnectionTime);
		scheduler.scheduleAtFixedRate(this::syncAccessCountsToDatabase, syncToDatabaseTime);

	}

	private boolean checkConnection() {
		try (RedisConnection connection = redisConnectionFactory.getConnection()) {
			log.info("Redis Database is active: {}", ZonedDateTime.now());
			return true;
		} catch (RedisConnectionFailureException e) {
			log.error("Couldn't connect on Redis Database: {}", ZonedDateTime.now());
			return false;
		}
	}

	private void syncAccessCountsToDatabase() {
		log.info("Syncing access counts and last access times from Redis to database");

		if (!checkConnection()) {
			log.warn("Failed to connect to Redis during sync");
			return;
		}

		Set<String> accessCountKeys = redisTemplate.keys("access_count:*");
		if (accessCountKeys != null && !accessCountKeys.isEmpty()) {
			for (String key : accessCountKeys) {
				String shortUrl = key.replace("access_count:", "");
				String value = redisTemplate.opsForValue().get(key);

				int accessCount = (value != null) ? Integer.parseInt(value) : 0;

				repository.findByShortUrl(shortUrl).ifPresent(url -> {
					url.setAccessCount(url.getAccessCount() + accessCount);
					repository.save(url);
				});

				redisTemplate.delete(key);
			}
		}

		Set<String> lastAccessKeys = redisTemplate.keys("last_access:*");
		if (lastAccessKeys != null && !lastAccessKeys.isEmpty()) {
			for (String key : lastAccessKeys) {
				String shortUrl = key.replace("last_access:", "");
				String lastAccessTimeStr = redisTemplate.opsForValue().get(key);

				if (lastAccessTimeStr != null) {
					ZonedDateTime lastAccessTime = ZonedDateTime.parse(lastAccessTimeStr);

					repository.findByShortUrl(shortUrl).ifPresent(url -> {
						url.setLastAccessTime(lastAccessTime);
						repository.save(url);
					});
				}

				redisTemplate.delete(key);
			}
		}
	}

}
