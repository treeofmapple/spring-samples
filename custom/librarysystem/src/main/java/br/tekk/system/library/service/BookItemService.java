package br.tekk.system.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.tekk.system.library.exception.BookItemNotFoundException;
import br.tekk.system.library.exception.BookNotFoundException;
import br.tekk.system.library.exception.MaxBookRentException;
import br.tekk.system.library.exception.UserNotFoundException;
import br.tekk.system.library.mapper.BookItemMapper;
import br.tekk.system.library.model.Book;
import br.tekk.system.library.model.BookItem;
import br.tekk.system.library.model.Status;
import br.tekk.system.library.model.User;
import br.tekk.system.library.repository.BookItemRepository;
import br.tekk.system.library.repository.BookRepository;
import br.tekk.system.library.repository.UserRepository;
import br.tekk.system.library.request.BookItemRequest;
import br.tekk.system.library.request.BookItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookItemService {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final BookItemRepository bookItemRepository;
	private final BookItemMapper mapper;

	public List<BookItemResponse> findAllBookItems() {
		List<BookItem> bookItems = bookItemRepository.findAll();
		if (bookItems.isEmpty()) {
			throw new BookItemNotFoundException("No books items found");
		}
		return bookItems.stream().map(mapper::fromBookItem).collect(Collectors.toList());
	}

	public BookItemResponse findById(Integer bookItemId) {
		return bookItemRepository.findById(bookItemId).map(mapper::fromBookItem)
				.orElseThrow(() -> new BookItemNotFoundException(
						String.format("No book item found with the provided ID:: %s", bookItemId)));
	}

	@Transactional
	public Integer createBookItem(BookItemRequest request) {
		final int Max_Books = 4;

		var book = bookRepository.findById(request.book())
				.orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + request.book()));

		var user = userRepository.findById(request.user())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.user()));

		if (bookItemRepository.countByUserIdAndBookIdAndStatus(user.getId(), book.getId(), Status.RENT) >= Max_Books) {
			throw new MaxBookRentException("User reached max rent limit for book ID: " + book.getId());
		}

		var bookItem = mapper.toBookItem(request);
		return bookItemRepository.save(bookItem).getId();
	}

	@Transactional
	public void updateBookItem(Integer bookItemId, BookItemRequest request) {
		var book = bookItemRepository.findById(bookItemId).orElseThrow(() -> new BookItemNotFoundException(
				String.format("Cannot update BookItem, no bookItem found with the provide ID:: %s", bookItemId)));
		mergerBookItem(book, request);
		bookItemRepository.save(book);
	}

	public void deleteBookItem(Integer bookItemId) {
		if (!bookItemRepository.existsById(bookItemId)) {
			throw new BookItemNotFoundException("Book not found with ID:: " + bookItemId);
		}
		bookItemRepository.deleteById(bookItemId);
	}

	private void mergerBookItem(BookItem bookItem, BookItemRequest request) {
		var user = User.builder().id(request.user()).build();

		if (request.user() != null && userRepository.existsById(request.user())) {
			bookItem.setUser(user);
		} else {
			throw new UserNotFoundException("User with provided ID does not exist.");
		}

		bookItem.setBook(Book.builder().id(request.book()).build());
		bookItem.setUser(user);
		bookItem.setStatus(request.status());
		bookItem.setRentStart(request.rentStart());
		bookItem.setRentEnd(request.rentEnd());
	}
	
	/*
	 * @Transactional public Status sellBookItem(Integer bookItemId) { var bookItem
	 * = bookItemRepository.findById(bookItemId).orElseThrow(() -> new
	 * BookItemNotFoundException( String.
	 * format("Cannot update BookItem, no bookItem found with the provide ID:: %s",
	 * bookItemId)));
	 * 
	 * bookItem.setStatus(Status.SOLD); bookItemRepository.save(bookItem);
	 * bookItem.getBook().setQuantity(bookItem.getBook().getQuantity() - 1); return
	 * bookItem.getStatus(); }
	 * 
	 * @Transactional public LocalDate startRentBook(Integer bookItemId,
	 * BookItemRequest request) { var bookItem =
	 * bookItemRepository.findById(bookItemId).orElseThrow(() -> new
	 * BookItemNotFoundException( String.
	 * format("Cannot update BookItem, no bookItem found with the provide ID:: %s",
	 * bookItemId))); bookItem.setStatus(Status.RENT);
	 * bookItem.setRentStart(request.rentStart() != null ? request.rentStart() :
	 * LocalDate.now());
	 * bookItem.getBook().setQuantity(bookItem.getBook().getQuantity() - 1); return
	 * request.rentStart(); }
	 * 
	 * @Transactional public LocalDate returnBookItem(Integer bookItemId,
	 * BookItemRequest request) { var bookItem =
	 * bookItemRepository.findById(bookItemId).orElseThrow(() -> new
	 * BookItemNotFoundException( String.
	 * format("Cannot update BookItem, no bookItem found with the provide ID:: %s",
	 * bookItemId)));
	 * 
	 * bookItem.setStatus(Status.RETURNED); bookItem.setRentEnd(request.rentEnd());
	 * bookItem.getBook().setQuantity(bookItem.getBook().getQuantity() + 1); return
	 * request.rentEnd(); }
	 */

}
