package com.tom.first.library.dto;

import java.time.LocalDate;

import com.tom.first.library.model.Book;
import com.tom.first.library.model.User;
import com.tom.first.library.model.enums.Status;

public record ItemResponse(
		
		BookDTO book,
		
		UserDTO user,
		
		Status status,
		
		LocalDate rentStart,
		
		LocalDate rentEnd
		
) {

    public record BookDTO(String title) {
        public BookDTO(Book book) {
            this(book != null ? book.getTitle() : null);
        }
    }

    public record UserDTO(String username) {
        public UserDTO(User user) {
            this(user != null ? user.getUsername() : null);
        }
    }
	
    public record ItemBookResponse(
    		
    		BookDTO book,
    		
    		UserDTO user,
    		
    		Status status
    		
    ) {
    	
    }
    
    
}
