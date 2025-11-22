package com.tom.first.library.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.library.model.Book;
import com.tom.first.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenerateData {

	private final BookRepository bookRepository;
	private final GenerateDataUtil dataUtil;

	@Transactional
	public Book processBook() {
		var gen = genBook();
		bookRepository.save(gen);
		return gen;
	}

	@Transactional
	public List<Book> processBooks() {
		List<Book> books = genBookOneAuthorEachTen();
		bookRepository.saveAll(books);
		return books;
	}

	private List<Book> genBookOneAuthorEachTen() {
		List<Book> books = new ArrayList<>();
		String author = dataUtil.generateAuthorNames();
		for (int i = 0; i < 10; i++) {
			Book book = new Book();
			book.setTitle(dataUtil.generateBookTitle());
			book.setAuthor(author);
			book.setLaunchYear(dataUtil.getRandomDate(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 12, 31)));
			book.setQuantity(dataUtil.getRandomInteger(1, 1000));
			book.setPrice(dataUtil.getRandonBigDecimal(1, 399.99));
			books.add(book);
		}
		return books;
	}
	
	private Book genBook() {
		Book book = new Book();
		book.setTitle(dataUtil.generateBookTitle());
		book.setAuthor(dataUtil.generateAuthorNames());
		book.setLaunchYear(dataUtil.getRandomDate(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 12, 31)));
		book.setQuantity(dataUtil.getRandomInteger(1, 1000));
		book.setPrice(dataUtil.getRandonBigDecimal(1, 399.99));
		return book;
	}

}
