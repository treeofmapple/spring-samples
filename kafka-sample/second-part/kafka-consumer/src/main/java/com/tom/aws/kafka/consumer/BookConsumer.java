package com.tom.aws.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tom.aws.kafka.book.BookDTO;
import com.tom.aws.kafka.book.BookMapper;
import com.tom.aws.kafka.book.BookRepository;

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
