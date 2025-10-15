package com.tom.aws.kafka.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public record BookResponse(
		
		Long id,
		
		String isbn,
		
		String title,
		
		String author,
		
		BigDecimal price,
		
		LocalDate publishedDate,
		
		Integer stockQuantity,
		
		ZonedDateTime createdAt,
		
		ZonedDateTime updatedAt
		
		
		) {

}
