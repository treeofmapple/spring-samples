package br.tekk.system.library.request.dto;

import br.tekk.system.library.model.Book;

public record BookDTO(Integer id) {
	public BookDTO(Book book) {
		this(book.getId());
	}
}
