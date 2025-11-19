package com.tom.kafka.consumer.consume;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tom.kafka.consumer.book.BookDTO;
import com.tom.kafka.consumer.book.BookMapper;
import com.tom.kafka.consumer.book.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookConsumer {

	private final BookRepository bookRepository;
	private final BookMapper mapper;

	@KafkaListener(topics = "${spring.kafka.consumer.topic.name}", containerFactory = "kafkaListenerContainerFactory")
	public void recieveBookData(BookDTO bookDto) {
		var bookEntity = mapper.toEntity(bookDto);
		log.info("Recieved Book: {}", bookDto.title());
		bookRepository.save(bookEntity);
	}

}
