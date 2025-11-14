package com.tom.kafka.consumer.book;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookUtils {

	private final BookRepository repository;

	public Book findById(long id) {
		return repository.findById(id).orElseThrow(() -> {
			return new RuntimeException("Book not found with id: " + id);
		});
	}

}
