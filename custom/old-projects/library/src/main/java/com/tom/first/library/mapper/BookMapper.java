package com.tom.first.library.mapper;

import org.springframework.stereotype.Component;

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.dto.BookResponse.BookUpdateResponse;
import com.tom.first.library.model.Book;

@Component
public class BookMapper {

	public Book toBooks(BookRequest request) {
		if (request == null) {
			return null;
		}

		return Book.builder()
				.title(request.title())
				.author(request.author())
				.quantity(request.quantity())
				.price(request.price())
				.launchYear(request.launchYear())
				.build();
	}

	public BookResponse fromBook(Book book) {
		return new BookResponse(
				book.getTitle(), 
				book.getAuthor(), 
				book.getQuantity(), 
				book.getPrice(),
				book.getLaunchYear(), 
				book.getCreatedDate()
				);
	}
	
	public BookUpdateResponse fromUpdateResponse(Book book) {
		return new BookUpdateResponse(
				book.getTitle(),
				book.getAuthor(),
				book.getQuantity(),
				book.getPrice(),
				book.getLaunchYear()
				);
	}
	
	public void mergeBook(Book book, BookRequest request) {
		book.setTitle(request.title());
		book.setAuthor(request.author());
		book.setQuantity(request.quantity());
		book.setPrice(request.price());
		book.setLaunchYear(request.launchYear());
	}

}
