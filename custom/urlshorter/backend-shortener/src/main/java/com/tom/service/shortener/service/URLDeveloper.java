package com.tom.service.shortener.service;

import org.springframework.stereotype.Service;

import com.tom.service.shortener.common.RedisUtils;
import com.tom.service.shortener.common.SecurityUtils;
import com.tom.service.shortener.dto.URLComplete;
import com.tom.service.shortener.mapper.URLMapper;
import com.tom.service.shortener.repository.URLRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class URLDeveloper {

	private final URLRepository repository;
	private final URLMapper mapper;
	private final SecurityUtils securityUtils;
	private final RedisUtils redisUtils;
	private final URLUtils urlUtils;

	public URLComplete findFullURL(String request) {
		var response = urlUtils.findByBothUrl(request);
		return mapper.toResponseComplete(response);
	}

	public void deleteURL(String request) {
		log.info("User on {} is deleting {}", securityUtils.getRequestingClientIp(), request);
		var dataDelete = urlUtils.findByBothUrl(request);
		repository.deleteById(dataDelete.getId());

		if (redisUtils.isRedisAvailable()) {
			redisUtils.removeKey(dataDelete.getShortUrl());
			redisUtils.removeKey(dataDelete.getOriginalUrl());
		} else {
			log.warn("Skipping Redis cleanup; Redis is unavailable.");
		}

	}

}
