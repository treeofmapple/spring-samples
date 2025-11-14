package com.tom.kafka.producer.book;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.kafka.producer.logic.GenerateData;
import com.tom.kafka.producer.produce.BookProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookService {

	private final BookMapper mapper;
	private final BookProducer bookProducer;
	private final GenerateData dataGeneration;

	@Transactional
	public BookResponse sendBookRandomToKafka() {
		var book = dataGeneration.processGenerateAnBook();
		bookProducer.sendBook(book);
		log.info("Sending Book: \nBook ID: {}, \nBook Title: {}", book.getId(), book.getTitle());
		return mapper.toResponse(book);
	}
	
	@Async
	public void generateBooks(int quantity) {
		log.info("Starting async generation of {} books...", quantity);
		dataGeneration.processGenerateBooks(quantity);
		log.info("Products finished generation");
	}

}
