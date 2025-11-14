package com.tom.kafka.consumer.book;

import java.util.List;

public record BookPageResponse(
		
		List<BookResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
