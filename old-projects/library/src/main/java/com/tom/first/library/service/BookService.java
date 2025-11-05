package com.tom.first.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookRequest.AuthorRequest;
import com.tom.first.library.dto.BookRequest.TitleRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.dto.BookResponse.BookUpdateResponse;
import com.tom.first.library.mapper.BookMapper;
import com.tom.first.library.model.Book;
import com.tom.first.library.repository.BookRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository repository;
	private final BookMapper mapper;

	public List<BookResponse> findAll() {
		List<Book> books = repository.findAll();
		if (books.isEmpty()) {
			throw new RuntimeException("No books found.");
		}
		return books.stream().map(mapper::fromBook).collect(Collectors.toList());
	}

	public BookResponse findByTitle(TitleRequest request) {
		return repository.findByTitle(request.title()).map(mapper::fromBook).orElseThrow(
				() -> new RuntimeException(String.format("No book was found with the Title: %s.", request.title())));
	}

	public List<BookResponse> findByAuthor(AuthorRequest request) {
		List<Book> books = repository.findByAuthor(request.author());
		if (books.isEmpty()) {
			throw new RuntimeException(String.format("No books from the author %s was found.", request.author()));
		}
		return books.stream().map(mapper::fromBook).collect(Collectors.toList());
	}

	public List<BookResponse> findByRangeLaunchYear(LocalDate firstDate, LocalDate lastDate) {
		List<Book> books = repository.findByLaunchYearBetween(firstDate, lastDate);
		if (books.isEmpty()) {
			throw new RuntimeException("No books found between the date range.");
		}
		return books.stream().map(mapper::fromBook).collect(Collectors.toList());
	}
	
	@Transactional
	public BookResponse createBook(BookRequest request) {
		if(repository.existsByTitle(request.title())) {
			throw new RuntimeException
			(String.format("A book with same title already exists %s", request.title()));
		}
		
		var book = repository.save(mapper.toBooks(request));
		return mapper.fromBook(book);
	}
	
	@Transactional
	public BookUpdateResponse updateBook(TitleRequest title, BookRequest request) {
		var book = repository.findByTitle(request.title()).orElseThrow(
				() -> new RuntimeException
				(String.format("No book was found with the Title: %s.", request.title())));
		mapper.mergeBook(book, request);
		repository.save(book);
		return mapper.fromUpdateResponse(book);
	}
	
	@Transactional
	public void deleteBookByTitle(TitleRequest request) {
		if(!repository.existsByTitle(request.title())) {
			throw new RuntimeException
			(String.format("No book was found with the Title: %s.", request.title()));
		}
		repository.deleteByTitle(request.title());
	}

}
