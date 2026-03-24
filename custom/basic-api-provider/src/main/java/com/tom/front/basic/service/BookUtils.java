package com.tom.front.basic.service;

import org.springframework.stereotype.Component;

import com.tom.front.basic.model.Book;
import com.tom.front.basic.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookUtils {

	private final BookRepository repository;
	
	public Book ensureExistsAndGetId(long id) {
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
