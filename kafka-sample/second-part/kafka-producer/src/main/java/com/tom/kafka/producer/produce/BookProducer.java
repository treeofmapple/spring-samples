package com.tom.kafka.producer.produce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tom.kafka.producer.book.Book;
import com.tom.kafka.producer.book.BookDTO;
import com.tom.kafka.producer.book.BookMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookProducer {

	@Value("${spring.kafka.producer.topic.name}")
	private String topicName;

	private final KafkaTemplate<String, BookDTO> kafkaTemplate;
	private final BookMapper mapper;

	public void sendBook(Book book) {
		var bookEntity = mapper.toKafka(book);
		kafkaTemplate.send(topicName, bookEntity);
	}

}
