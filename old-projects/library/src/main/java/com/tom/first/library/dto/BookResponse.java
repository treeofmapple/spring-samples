package com.tom.first.library.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
		
		String title,
		
		String author,
		
		int quantity,
		
		double price,
		
		LocalDate launchYear,
		
		LocalDateTime createdDate
		

) {

	public record BookUpdateResponse(

		String title,
		
		String author,
		
		int quantity,
		
		double price,
		
		LocalDate launchYear

	) {
	}
	
}
