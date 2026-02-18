package com.tom.front.basic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.front.basic.common.IsbnGenerator;
import com.tom.front.basic.common.SystemUtils;
import com.tom.front.basic.dto.BookPageResponse;
import com.tom.front.basic.dto.BookRequest;
import com.tom.front.basic.dto.BookResponse;
import com.tom.front.basic.dto.UpdateRequest;
import com.tom.front.basic.mapper.BookMapper;
import com.tom.front.basic.model.Book;
import com.tom.front.basic.repository.BookRepository;
import com.tom.front.basic.repository.BookSpecification;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final BookRepository repository;
	private final BookMapper mapper;
	private final BookUtils bookUtils;
	private final SystemUtils utils;
	private final IsbnGenerator isbnGenerator; // Core Logic

	@Transactional(readOnly = true)
	public BookPageResponse findBookByParams(int page, String title, String author, LocalDate startDate,
			LocalDate endDate, HttpServletRequest httpRequest) {
		String userIp = utils.getRequestingClientIp(httpRequest);
		log.info("IP {}", userIp);

		Specification<Book> spec = BookSpecification.findByCriteria(title, author, startDate, endDate);

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Book> books = repository.findAll(spec, pageable);
		return mapper.toBookPageResponse(books);
	}

	@Transactional
	public BookResponse createBook(BookRequest request, HttpServletRequest httpRequest) {
		String userIp = utils.getRequestingClientIp(httpRequest);
		log.info("IP {}", userIp);

		try {
			bookUtils.ensureUniqueName(request.title());

			var newBook = mapper.build(request);
			String isbn = isbnGenerator.generateIsbn13("978", "1");
			newBook.setIsbn(isbn);

			var savedBook = repository.save(newBook);

			log.info("");
			return mapper.toResponse(savedBook);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public BookResponse editBook(long id, UpdateRequest request, HttpServletRequest httpRequest) {
		String userIp = utils.getRequestingClientIp(httpRequest);
		log.info("IP {}", userIp);

		var bookToUpdate = bookUtils.ensureExistsAndGetId(id);
		
		mapper.updateBookFromRequest(bookToUpdate, request);

		var savedBook = repository.save(bookToUpdate);
		
		log.info("IP {}", userIp);
		return mapper.toResponse(savedBook);
	}

	@Transactional
	public BookResponse updateIsbn(long id, HttpServletRequest httpRequest) {
		String userIp = utils.getRequestingClientIp(httpRequest);
		log.info("IP {}", userIp);

		var bookToUpdate = bookUtils.ensureExistsAndGetId(id);
		String isbn = isbnGenerator.generateIsbn13("978", "1");
		bookToUpdate.setIsbn(isbn);
		
		var savedBook = repository.save(bookToUpdate);
		
		return mapper.toResponse(savedBook);
	}

	@Transactional
	public void deleteBook(long id, HttpServletRequest httpRequest) {
		String userIp = utils.getRequestingClientIp(httpRequest);
		log.info("Attempting to delete book with ID {} for user at IP {}", id, userIp);

	    if (!repository.existsById(id)) {
	        throw new RuntimeException("Cannot delete. Book not found with id: " + id);
	    }
		
		repository.deleteById(id);
		log.info("Successfully deleted book with ID {}", id);
	}

}
