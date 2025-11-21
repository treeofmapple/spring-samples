package com.tom.first.vehicle.processes;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.first.vehicle.dto.VehicleOutboxBuild;
import com.tom.first.vehicle.mapper.VehicleMapper;
import com.tom.first.vehicle.model.enums.EventType;
import com.tom.first.vehicle.processes.events.VehicleCreatedEvent;
import com.tom.first.vehicle.processes.events.VehicleDeletedEvent;
import com.tom.first.vehicle.repository.VehicleOutboxRepository;
import com.tom.first.vehicle.repository.VehicleSearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class VehicleIndexer {

	private final VehicleSearchRepository searchRepository;
	private final VehicleOutboxRepository outboxRepository;
	private final VehicleMapper mapper;
	private final ObjectMapper objectMapper;

	@Async
	@EventListener
	public void handleVehicleCreated(VehicleCreatedEvent event) {
		var doc = mapper.build(event.vehicle());
		saveOutboxEvent(EventType.CREATED, event);
		searchRepository.save(doc);
	}

	@Async
	@EventListener
	public void handleVehicleDeletion(VehicleDeletedEvent event) {
		if (searchRepository.existsByPlate(event.plate())) {
			saveOutboxEvent(EventType.DELETED, event);
			searchRepository.deleteByPlate(event.plate());
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

		var event = mapper.build(new VehicleOutboxBuild(eventType, json, ZonedDateTime.now(ZoneOffset.UTC)));
		outboxRepository.save(event);
	}

}
