package com.tom.kafka.producer.book;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookUtils {

	private final BookRepository repository;

	public Book findById(long id) {
		return repository.findById(id).orElseThrow(() -> {
			throw new RuntimeException("Book not found with id: " + id);
		});
	}

	public void ensureUniqueName(String title) {
		if (repository.existsByTitle(title)) {
			throw new RuntimeException("Name already exists");
		}
	}

}
