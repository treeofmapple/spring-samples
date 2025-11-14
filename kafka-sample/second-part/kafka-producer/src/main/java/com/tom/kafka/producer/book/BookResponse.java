package com.tom.kafka.producer.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record BookResponse(
		
		Long id,
		
		String isbn,
		
		String title,
		
		String author,
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00") 
		BigDecimal price,
		
		LocalDate publishedDate,
		
		Integer stockQuantity,
		
		ZonedDateTime createdAt,
		
		ZonedDateTime updatedAt
		
		
		) {

}
