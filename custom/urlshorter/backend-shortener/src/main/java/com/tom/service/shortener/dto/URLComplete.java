package com.tom.service.shortener.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record URLComplete(
		
		UUID id,
		String originalUrl,
		String shortUrl,
		int accessCount,
		ZonedDateTime expirationTime,
		ZonedDateTime dateCreated,
		ZonedDateTime lastAccessTime
		
		) {

}
