package com.tom.first.library.processes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.library.dto.BookOutboxBuilder;
import com.tom.first.library.mapper.BookMapper;
import com.tom.first.library.model.BookDocument;
import com.tom.first.library.model.enums.EventType;
import com.tom.first.library.processes.events.BookCreatedEvent;
import com.tom.first.library.processes.events.BookDeletedEvent;
import com.tom.first.library.processes.events.BookListCreatedEvent;
import com.tom.first.library.repository.BookOutboxRepository;
import com.tom.first.library.repository.BookSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class BookIndexer {

	private final BookSearchRepository searchRepository;
	private final BookOutboxRepository outboxRepository;
	private final BookMapper mapper;
	private final ObjectMapper objectMapper;

	@Async
	@EventListener
	public void handleVehicleCreated(BookCreatedEvent event) {
		var doc = mapper.build(event.book());
		saveOutboxEvent(EventType.CREATED, doc);
		searchRepository.save(doc);
	}
	
	@Async
	@EventListener
	public void handleMultipleVehicleCreated(BookListCreatedEvent event) {
		List<BookDocument> doc = mapper.build(event.books());
		saveOutboxEvent(EventType.CREATED, event);
		searchRepository.saveAll(doc);
	}

	@Async
	@EventListener
	public void handleVehicleDeletion(BookDeletedEvent event) {
		if (searchRepository.existsByTitle(event.title())) {
			saveOutboxEvent(EventType.DELETED, event);
			searchRepository.deleteByTitle(event.title());
		} else {
			log.warn("Item don't exist on the elastic search");
		}
	}

	private void saveOutboxEvent(EventType eventType, Object payload) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		var event = mapper.build(new BookOutboxBuilder(eventType, json, ZonedDateTime.now(ZoneOffset.UTC)));
		outboxRepository.save(event);
	}

}
