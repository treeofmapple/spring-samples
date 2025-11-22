package com.tom.first.simple.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.simple.model.EvaluationDocument;
import com.tom.first.simple.model.EvaluationOutbox;
import com.tom.first.simple.model.enums.EventType;
import com.tom.first.simple.repository.EvaluationOutboxRepository;
import com.tom.first.simple.repository.EvaluationSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class ElasticSearchSchedules {

	private final EvaluationSearchRepository searchRepository;
	private final EvaluationOutboxRepository outboxRepository;
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

	private void processEvent(EvaluationOutbox event) throws Exception {
		switch (event.getEventType()) {

		case EventType.CREATED -> {
			var doc = objectMapper.readValue(event.getPayload(), EvaluationDocument.class);
			searchRepository.save(doc);
		}

		case EventType.DELETED -> {
			String plate = objectMapper.readValue(event.getPayload(), String.class);
			searchRepository.deleteBySubject(plate);
		}
		}
	}

	private void markAsProcessed(EvaluationOutbox event) {
		event.setProcessed(true);
		outboxRepository.save(event);
	}
}
