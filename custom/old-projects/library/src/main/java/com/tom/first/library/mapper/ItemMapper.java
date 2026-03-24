package com.tom.first.library.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.tom.first.library.dto.ItemResponse;
import com.tom.first.library.dto.ItemResponse.ItemBookResponse;
import com.tom.first.library.model.Book;
import com.tom.first.library.model.BookItem;
import com.tom.first.library.model.User;

@Component
public class ItemMapper {

	public void mergeItem(BookItem item, User user, Book book) {
		item.setBook(book);
		item.setUser(user);
	}
	
	public void rentDate(BookItem itemBook) {
		itemBook.setRentStart(LocalDate.now());
		itemBook.setRentEnd(LocalDate.now().plusDays(30));
	}

	public BookItem toBookItem(Book book, User user) {
		if (book == null || user == null) {
			return null;
		}
		return BookItem.builder()
				.book(book)
				.user(user)
				.build();
	}

	public ItemResponse fromBookItem(BookItem item) {
		return new ItemResponse(
				new ItemResponse.BookDTO(item.getBook()), 
				new ItemResponse.UserDTO(item.getUser()), 
				item.getStatus(), 
				item.getRentStart(),
				item.getRentEnd()
				);
	}

	public ItemBookResponse fromBookItemUser(BookItem item) {
		return new ItemBookResponse(
				new ItemResponse.BookDTO(item.getBook()), 
				new ItemResponse.UserDTO(item.getUser()),
				item.getStatus()
				);
	}
	
}
