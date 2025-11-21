package com.tom.first.username.processes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.username.dto.UserOutboxBuild;
import com.tom.first.username.mapper.UserMapper;
import com.tom.first.username.model.enums.EventType;
import com.tom.first.username.processes.events.UserCreatedEvent;
import com.tom.first.username.processes.events.UserDeletedEventByEmail;
import com.tom.first.username.processes.events.UserDeletedEventByUsername;
import com.tom.first.username.repository.UserOutboxRepository;
import com.tom.first.username.repository.UserSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class UsernameIndexer {

	private final UserSearchRepository searchRepository;
	private final UserOutboxRepository outboxRepository;
	private final UserMapper mapper;
	private final ObjectMapper objectMapper;

	@Async
	@EventListener
	public void handleVehicleCreated(UserCreatedEvent event) {
		var doc = mapper.build(event.username());
		saveOutboxEvent(EventType.CREATED, event);
		searchRepository.save(doc);
	}

	@Async
	@EventListener
	public void handleVehicleDeletionByUsername(UserDeletedEventByUsername event) {
		if (searchRepository.existsByName(event.username())) {
			saveOutboxEvent(EventType.DELETED, event);
			searchRepository.deleteByName(event.username());
		} else {
			log.warn("Item don't exist on the elastic search");
		}
	}
	
	@Async
	@EventListener
	public void handleVehicleDeletionByEmail(UserDeletedEventByEmail event) {
		if (searchRepository.existsByEmail(event.email())) {
			saveOutboxEvent(EventType.DELETED, event);
			searchRepository.deleteByEmail(event.email());
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

		var event = mapper.build(new UserOutboxBuild(eventType, json, ZonedDateTime.now(ZoneOffset.UTC)));
		outboxRepository.save(event);
	}

}
