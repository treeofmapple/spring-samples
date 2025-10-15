package com.tom.aws.kafka.book;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookDTO(
		
	    String isbn,
	    String title,
	    String author,
	    BigDecimal price,
	    LocalDate publishedDate,
	    Integer stockQuantity
		
		) {

}
