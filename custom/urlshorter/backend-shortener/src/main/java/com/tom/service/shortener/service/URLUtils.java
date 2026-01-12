package com.tom.service.shortener.service;

import org.springframework.stereotype.Component;

import com.tom.service.shortener.exception.NotFoundException;
import com.tom.service.shortener.logic.URLEncoding;
import com.tom.service.shortener.model.URL;
import com.tom.service.shortener.repository.URLRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class URLUtils {

	private final URLEncoding encoder;
	private final URLRepository repository;

	public URL findByShortUrl(String query) {
		return repository.findByShortUrl(query).orElseThrow(() -> {
			throw new NotFoundException("Short Url Not Found");
		});
	}

	public URL findByOriginalUrl(String query) {
		return repository.findByOriginalUrl(query).orElseThrow(() -> {
			throw new NotFoundException("Original Url Not Found");
		});
	}

	public URL findByBothUrl(String query) {
		return repository.findByShortUrl(query)
				.or(() -> repository.findByOriginalUrl(query))
				.orElseThrow(() -> {
			throw new NotFoundException("Url Not Found");
		});
	}
	
	public String generateRandomUrl() {
		String shortUrl;
		do {
			shortUrl = encoder.generateURL();
		} while(repository.existsByShortUrl(shortUrl));
		return shortUrl;
	}
	
}
