package com.tom.first.username.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.username.model.UserDocument;
import com.tom.first.username.model.UserOutbox;
import com.tom.first.username.model.enums.EventType;
import com.tom.first.username.repository.UserOutboxRepository;
import com.tom.first.username.repository.UserSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class ElasticSearchSchedules {

	private final UserSearchRepository searchRepository;
	private final UserOutboxRepository outboxRepository;
	private final ObjectMapper objectMapper;

	@Scheduled(fixedDelay = 300_000)
	public void syncWithElasticsearch() {
		var events = outboxRepository.findByProcessedFalse();
		for (var event : events) {
			try {
				processEvent(event);
				markAsProcessed(event);
			} catch (Exception e) {
				log.error("Failed to sync event {}", event.getId(), e);
			}
		}
	}

	private void processEvent(UserOutbox event) throws Exception {
		switch (event.getEventType()) {

		case EventType.CREATED -> {
			var doc = objectMapper.readValue(event.getPayload(), UserDocument.class);
			searchRepository.save(doc);
		}

		case EventType.DELETED -> {
			String plate = objectMapper.readValue(event.getPayload(), String.class);
			searchRepository.deleteByEmail(plate);
		}
		}
	}

	private void markAsProcessed(UserOutbox event) {
		event.setProcessed(true);
		outboxRepository.save(event);
	}
}
