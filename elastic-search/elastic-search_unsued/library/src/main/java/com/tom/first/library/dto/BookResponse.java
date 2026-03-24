package com.tom.first.library.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
		
		String title,
		String author,
		Integer quantity,
		Double price,
		LocalDate launchYear,
		LocalDateTime createdDate
		

) {

}
