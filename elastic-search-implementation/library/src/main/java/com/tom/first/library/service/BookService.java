package com.tom.first.library.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.library.dto.BookItemResponse;
import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.logic.GenerateData;
import com.tom.first.library.mapper.BookMapper;
import com.tom.first.library.model.Book;
import com.tom.first.library.processes.events.BookCreatedEvent;
import com.tom.first.library.processes.events.BookDeletedEvent;
import com.tom.first.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookService {

	// private final BookItemRepository bookItemRepository;
	// private final RentHistoryRepository historyRepository;
	// private final UserService service;

	private final AtomicBoolean running = new AtomicBoolean(false);
	private final ThreadPoolTaskExecutor executor;
	private final ApplicationEventPublisher eventPublisher;
	private final BookRepository bookRepository;
	private final BookMapper mapper;
	private final GenerateData dataGeneration;

	@Transactional(readOnly = true)
	public BookResponse findById(long query) {
		return bookRepository.findById(query).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Book with id '%s' was not found.", query));
		});
	}

	@Transactional(readOnly = true)
	public BookResponse findByTitle(String title) {
		return bookRepository.findByTitle(title).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Book with %s was not found.", title));
		});
	}

	@Transactional(readOnly = true)
	public List<BookResponse> findAllBooksFromAuthor(String author) {
		List<Book> books = bookRepository.findByAuthor(author);
		if (books.isEmpty()) {
			throw new RuntimeException(String.format("No books from the author %s was found.", author));
		}
		return books.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public BookItemResponse findRentBookId(long query) {
		return null;
	}

	@Transactional(readOnly = true)
	public BookItemResponse findAllRentBooks() {
		return null;
	}

	public void startStreaming(int speed) {
		if (running.compareAndSet(false, true)) {
			executor.submit(() -> sendLoop(speed));
			log.info("Sending Evaluation Data");
		} else {
			log.warn("Stopped Sending Evaluation Data");
		}
	}

	public void stopStreaming() {
		running.set(false);
		log.warn("Stopped Sending Evaluation Data");
	}

	private void sendLoop(int speed) {
		while (running.get()) {
			var book = dataGeneration.processGenerateAnEvaluation();
			eventPublisher.publishEvent(book);
			try {
				Thread.sleep(speed > 0 ? speed : 100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Transactional
	public BookResponse createBook(BookRequest request) {
		if (bookRepository.existsByTitle(request.title())) {
			throw new RuntimeException(String.format("A book with same title already exists %s", request.title()));
		}
		var book = mapper.build(request);
		bookRepository.save(book);

		eventPublisher.publishEvent(new BookCreatedEvent(book));
		return mapper.toResponse(book);
	}

	/* add book to rent history */

	@Transactional
	public void deleteBookByTitle(String title) {
		if (!bookRepository.existsByTitle(title)) {
			throw new RuntimeException(String.format("No book was found with the Title: %s.", title));
		}
		eventPublisher.publishEvent(new BookDeletedEvent(title));
		bookRepository.deleteByTitle(title);
	}

	/* remove book from rent history */

}
