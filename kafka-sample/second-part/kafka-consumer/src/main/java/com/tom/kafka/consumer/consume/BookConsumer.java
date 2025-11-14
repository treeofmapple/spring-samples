package com.tom.kafka.consumer.consume;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tom.kafka.consumer.book.BookDTO;
import com.tom.kafka.consumer.book.BookMapper;
import com.tom.kafka.consumer.book.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookConsumer {
	
	private final BookRepository bookRepository;
	private final BookMapper mapper;

	@KafkaListener(topics = "${spring.kafka.consumer.topic.name}",
			containerFactory = "kafkaListenerContainerFactory")
	public void recieveBookData(BookDTO bookDto) {
		var bookEntity = mapper.toEntity(bookDto); 
		bookRepository.save(bookEntity);
	}

}
