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
import com.tom.first.vehicle.model.VehicleOutbox;
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
		var outbox = saveOutboxEvent(EventType.CREATED, doc);
		
		try {
			searchRepository.save(doc);
	        outbox.setProcessed(true);
	        outboxRepository.save(outbox);
		} catch (Exception e) {
	        log.error("Immediate ES write failed, fallback enabled", e);
		}
	}

	@Async
	@EventListener
	public void handleVehicleDeletion(VehicleDeletedEvent event) {
	    var outbox = saveOutboxEvent(EventType.DELETED, event.plate());
	    try {
	        searchRepository.deleteByPlate(event.plate()); 
	        outbox.setProcessed(true);
	        outboxRepository.save(outbox);
	    } catch (Exception e) {
	        log.error("Immediate ES delete failed, fallback enabled", e);
	    }
	}

	private VehicleOutbox saveOutboxEvent(EventType eventType, Object payload) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		var event = mapper.build(new VehicleOutboxBuild(eventType, json, ZonedDateTime.now(ZoneOffset.UTC)));
		return outboxRepository.save(event);
	}

}
