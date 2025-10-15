package com.tom.aws.kafka.book;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record UpdateRequest(
		
        @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters.")
        String title,

        @Size(min = 2, max = 150, message = "Author name must be between 2 and 150 characters.")
        String author,
		
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater.")
        @Digits(integer = 8, fraction = 2, message = "Price can have up to 8 integer digits and 2 fractional digits.")
		BigDecimal price,
		
		@PastOrPresent(message = "Published date cannot be in the future.")
		LocalDate publishedDate,
		
        @Min(value = 0, message = "Stock quantity must be zero or greater.")
		Integer stockQuantity

		) {

}
