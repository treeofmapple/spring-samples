package com.tom.first.library.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.PastOrPresent;

public record BookRequest(
        
        @NotBlank(message = "Title cannot be empty")
        String title,
        
        String author,
        
        @Min(value = 1, message = "Minimum quantity is 1 unit") 
        @Max(value = 10, message = "Maximum quantity is 10 units")
        int quantity,
        
        @PositiveOrZero(message = "Price must be zero or positive") 
        double price,
        
        LocalDate launchYear
        
) {

	public record TitleRequest(
			
        @NotBlank(message = "Title cannot be empty")
        String title
			
	) {
	}
	
	
	public record AuthorRequest(
			
		@NotBlank(message = "Author cannot be empty")
        String author	
			
	) {
	}
	
	public record YearRangeRequest(

		@NotBlank(message = "FirstDate cannot be empty")
		@PastOrPresent(message = "Can't find books into future")
        LocalDate firstDate,
		
        @NotBlank(message = "LastDate cannot be empty")
		@PastOrPresent(message = "Can't find books into future")
		LocalDate lastDate
	) {
	}
	
	
}
