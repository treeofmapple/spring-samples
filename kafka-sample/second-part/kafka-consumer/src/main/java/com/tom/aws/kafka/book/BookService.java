package com.tom.aws.kafka.book;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.aws.kafka.common.SecurityUtils;

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
	private final SecurityUtils securityUtils;

	@Transactional(readOnly = true)
	public BookPageResponse searchBookByParams(int page, String isbn, String title, String author, LocalDate startDate,
			LocalDate endDate) {
		Specification<Book> spec = BookSpecification.findByCriteria(isbn, title, author, startDate, endDate);

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Book> books = repository.findAll(spec, pageable);
		return mapper.toBookPageResponse(books);
	}
	
	@Transactional(readOnly = true)
	public BookResponse searchBookById(long query) {
		var book = bookUtils.findById(query);
		return mapper.toResponse(book);
	}

	@Transactional
	public BookResponse updateBook(long query, UpdateRequest request) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {}: is editing book: id {}", userIp, query);
		var bookToUpdate = bookUtils.findById(query);
		
		mapper.updateBookFromRequest(bookToUpdate, request);
		var savedBook = repository.save(bookToUpdate);
		
		log.info("IP {}", userIp);
		return mapper.toResponse(savedBook);
	}

	@Transactional
	public void deleteBook(long query) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is deleting book: id {}", userIp, query);
		var book = bookUtils.findById(query);
		
		repository.deleteById(book.getId());
		log.info("Successfully deleted book with ID {}", query);
	}
	
}
