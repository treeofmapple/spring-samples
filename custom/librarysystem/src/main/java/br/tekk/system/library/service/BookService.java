package br.tekk.system.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.tekk.system.library.exception.BookAlreadyExistsException;
import br.tekk.system.library.exception.BookNotFoundException;
import br.tekk.system.library.mapper.BookMapper;
import br.tekk.system.library.model.Book;
import br.tekk.system.library.repository.BookRepository;
import br.tekk.system.library.request.BookRequest;
import br.tekk.system.library.request.BookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository repository;
	private final BookMapper mapper;

	public List<BookResponse> findAllBooks() {
		List<Book> books = repository.findAll();
		if (books.isEmpty()) {
			throw new BookNotFoundException("No books found");
		}
		return books.stream().map(mapper::fromBooks).collect(Collectors.toList());
	}

	public BookResponse findById(Integer bookId) {
		return repository.findById(bookId).map(mapper::fromBooks).orElseThrow(
				() -> new BookNotFoundException(String.format("No books found with the provided ID:: %s", bookId)));
	}

	public Integer createBook(BookRequest request) {
		var title = request.title();
		if (repository.existsByTitle(title)) {
			throw new BookAlreadyExistsException(String.format("Book with the same titulo already exists", title));
		}
		var book = repository.save(mapper.toBooks(request));
		return book.getId();
	}

	public void updateBook(Integer id, BookRequest request) {
		var book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(
				String.format("Cannot update Book, no book found with the provided ID:: %s", id)));
		mergerBook(book, request);
		repository.save(book);
	}

	public void deleteBook(Integer bookId) {
		if (!repository.existsById(bookId)) {
			throw new BookNotFoundException("Book not found with ID:: " + bookId);
		}
		repository.deleteById(bookId);
	}

	private void mergerBook(Book book, BookRequest request) {
		book.setTitle(request.title());
		book.setAuthor(request.author());
		book.setQuantity(request.quantity());
		book.setPrice(request.price());
		book.setAno(request.ano());
	}

}
