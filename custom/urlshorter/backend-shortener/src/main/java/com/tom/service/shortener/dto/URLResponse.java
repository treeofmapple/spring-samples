package com.tom.service.shortener.dto;

import java.time.ZonedDateTime;

public record URLResponse(
		
		String originalUrl,
		String shortUrl,
		int accessCount,
		ZonedDateTime expirationTime
		
		) {

}
