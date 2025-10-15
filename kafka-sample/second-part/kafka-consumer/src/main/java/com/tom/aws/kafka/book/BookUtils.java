package com.tom.aws.kafka.book;

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
	
	public void ensureUniqueName(String title) {
		if (repository.existsByTitle(title)) {
			throw new IllegalStateException("");
		}
	}

	public void checkIfAlreadyExists(Book currentBook, String newName) {
		repository.findByTitle(newName).ifPresent(existentBook -> {
			if (!existentBook.getId().equals(currentBook.getId())) {
				throw new IllegalStateException("The title '" + newName + "' is already in use by another book.");
			}
		});
	}
	
}
