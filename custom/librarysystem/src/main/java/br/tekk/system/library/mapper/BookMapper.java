package br.tekk.system.library.mapper;

import org.springframework.stereotype.Service;

import br.tekk.system.library.model.Book;
import br.tekk.system.library.request.BookRequest;
import br.tekk.system.library.request.BookResponse;

@Service
public class BookMapper {

	public Book toBooks(BookRequest request) {
		if (request == null) {
			return null;
		}

		return Book.builder().title(request.title()).author(request.author()).quantity(request.quantity())
				.price(request.price()).ano(request.ano()).build();
	}

	public BookResponse fromBooks(Book book) {
		return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getQuantity(), book.getPrice(),
				book.getAno());
	}
}
