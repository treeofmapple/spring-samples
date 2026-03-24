package com.tom.first.library.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        
        @NotBlank(message = "Title cannot be empty")
        String title,
        
        String author,
        
        @Min(value = 1, message = "Minimum quantity is 1 unit") 
        @Max(value = 1000, message = "Maximum quantity is 10 units")
        int quantity,
        
    	@DecimalMin(value = "0.00", message = "Price can't be negative ")
        BigDecimal price,
        
        LocalDate launchYear
        
) {

}
