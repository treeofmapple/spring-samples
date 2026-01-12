package com.tom.service.shortener.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tom.service.shortener.common.DurationUtils;
import com.tom.service.shortener.common.RedisUtils;
import com.tom.service.shortener.dto.URLRequest;
import com.tom.service.shortener.dto.URLResponse;
import com.tom.service.shortener.exception.DuplicateException;
import com.tom.service.shortener.mapper.URLMapper;
import com.tom.service.shortener.repository.URLRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class URLService {

	@Value("${application.url.expiration-time:2D}")
	private String expirationTime;

	/**
	 * Defines the retention period for user access data (e.g., access count) in
	 * Redis. This duration MUST be set to a value higher than the execution
	 * interval of the {@code syncToDatabase} job in the {@code RedisScheduled}
	 * class.
	 * <p>
	 * This ensures that the access data remains in Redis long enough for the
	 * scheduled job to persist it to the main database before it expires.
	 */
	
	@Value("${application.url.user-increment:20M}")
	private String userIncrement;

	private final URLRepository repository;
	private final URLMapper mapper;
	private final RedisUtils redisUtils;
	private final URLUtils urlUtils;

	public URLResponse findBasicURL(String request) {
		var url = urlUtils.findByBothUrl(request);
		return mapper.toResponse(url);
	}

	public URLResponse shortenURL(URLRequest request) {
		log.info("Shortening URL: {}", request.url());

		String shortUrl = urlUtils.generateRandomUrl();

		var urlEntity = mapper.buildAtributes(shortUrl, request.url(),
				ZonedDateTime.now().plus(DurationUtils.parseDuration(expirationTime)));

		try {
			repository.save(urlEntity);
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateException("Url already exists");
		}

		redisUtils.insertRedisData(shortUrl, request.url(), true);

		String serverPath = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

		urlEntity.setShortUrl(serverPath + "/" + shortUrl);
		var response = mapper.toResponse(urlEntity);

		log.info("Successfully shortened URL: {} -> {}", request.url(), urlEntity.getShortUrl());
		return response;
	}

	public String redirectURL(String request) {
		String dataRequest = request;
		String originalUrl = null;

		if (redisUtils.isRedisAvailable()) {
			originalUrl = redisUtils.retrieveRedisData(dataRequest);
		} else {
			log.warn("Skipping Redis request; Redis is unavailable.");
		}

		if (originalUrl == null) {
			var urlFromDb = urlUtils.findByShortUrl(dataRequest);

			originalUrl = urlFromDb.getOriginalUrl();

			if (redisUtils.isRedisAvailable()) {
				redisUtils.insertRedisData(dataRequest, originalUrl, true);
			} else {
				log.warn("Skipping Redis request; Redis is unavailable.");
			}
		}

		// This increments access count
		String accessCount = "access_count:" + dataRequest;
		String lastAccess = "last_access:" + dataRequest;
		String timeAccess = ZonedDateTime.now().toString();

		redisUtils.insertRedisDataIncrement(accessCount, lastAccess, timeAccess,
				DurationUtils.parseDuration(userIncrement));
		return originalUrl;
	}
	
	public URLResponse extendUrlExpiration(String request) {
		var url = urlUtils.findByShortUrl(request);
		url.setExpirationTime(ZonedDateTime.now().plus(DurationUtils.parseDuration(expirationTime)));
		repository.save(url);
		
		return mapper.toResponse(url);
	}

}