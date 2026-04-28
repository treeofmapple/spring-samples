package br.tekk.system.library.mapper;

import org.springframework.stereotype.Service;

import br.tekk.system.library.model.Book;
import br.tekk.system.library.model.BookItem;
import br.tekk.system.library.model.User;
import br.tekk.system.library.request.BookItemRequest;
import br.tekk.system.library.request.BookItemResponse;
import br.tekk.system.library.request.dto.BookDTO;
import br.tekk.system.library.request.dto.UserDTO;

@Service
public class BookItemMapper {

	public BookItem toBookItem(BookItemRequest request) {
		if (request == null) {
			return null;
		}

		return BookItem.builder().book(Book.builder().id(request.book()).build())
				.user(User.builder().id(request.user()).build()).status(request.status()).rentStart(request.rentStart())
				.rentEnd(request.rentEnd()).build();
	}

	public BookItemResponse fromBookItem(BookItem item) {
		return new BookItemResponse(item.getId(), new BookDTO(item.getBook().getId()),
				new UserDTO(item.getUser().getId()), item.getStatus(), item.getRentStart(), item.getRentEnd());
	}

}
