package com.tom.first.library.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.tom.first.library.model.BookItem;

public record UserResponse(

		String username,

		String email,

		int age,

		LocalDateTime createdDate,

		UserBookItems book

) {
	
	public record UserUpdateResponse(
		String username, 
		String email, 
		int age) {
	}
	
	public record UserBookItems(List<BookItemSummary> items) {
		public static UserBookItems fromBookItems(List<BookItem> bookItems) {
			if (bookItems == null) {
				return new UserBookItems(List.of());
			}

			List<BookItemSummary> summaries = bookItems.stream()
					.map(item -> new BookItemSummary(item.getBook().getTitle(), item.getRentStart(), item.getRentEnd()))
					.collect(Collectors.toList());

			return new UserBookItems(summaries);
		}
		
	}

	public record BookItemSummary(String title, LocalDate rentStart, LocalDate rentEnd) {

	}
}
