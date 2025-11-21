package com.tom.first.simple.processes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.simple.dto.EvaluationOutboxBuild;
import com.tom.first.simple.mapper.EvaluationMapper;
import com.tom.first.simple.model.enums.EventType;
import com.tom.first.simple.processes.events.EvaluationCreatedEvent;
import com.tom.first.simple.processes.events.EvaluationDeletedEvent;
import com.tom.first.simple.repository.EvaluationOutboxRepository;
import com.tom.first.simple.repository.EvaluationSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class EvaluationIndexer {

	private final EvaluationSearchRepository searchRepository;
	private final EvaluationOutboxRepository outboxRepository;
	private final EvaluationMapper mapper;
	private final ObjectMapper objectMapper;

	@Async
	@EventListener
	public void handleVehicleCreated(EvaluationCreatedEvent event) {
		var doc = mapper.build(event.evaluation());
		saveOutboxEvent(EventType.CREATED, event);
		searchRepository.save(doc);
	}

	@Async
	@EventListener
	public void handleVehicleDeletion(EvaluationDeletedEvent event) {
		if (searchRepository.existsBySubject(event.subject())) {
			saveOutboxEvent(EventType.DELETED, event);
			searchRepository.deleteBySubject(event.subject());
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

		var event = mapper.build(new EvaluationOutboxBuild(eventType, json, ZonedDateTime.now(ZoneOffset.UTC)));
		outboxRepository.save(event);
	}

}
