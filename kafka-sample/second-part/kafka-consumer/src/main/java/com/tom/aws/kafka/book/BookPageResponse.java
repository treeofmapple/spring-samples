package com.tom.aws.kafka.book;

import java.util.List;

public record BookPageResponse(
		
		List<BookResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
