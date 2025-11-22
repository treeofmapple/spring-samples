package com.tom.first.vehicle.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.mapper.VehicleMapper;
import com.tom.first.vehicle.processes.events.VehicleCreatedEvent;
import com.tom.first.vehicle.processes.events.VehicleDeletedEvent;
import com.tom.first.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleService {

	private final ApplicationEventPublisher eventPublisher;
	private final VehicleRepository repository;
	private final VehicleMapper mapper;

	@Transactional(readOnly = true)
	public VehicleResponse findById(long query) {
		return repository.findById(query).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Vehicle with id '%s' was not found.", query));
		});
	}

	@Transactional(readOnly = true)
	public VehicleResponse findByPlate(String plate) {
		return repository.findByPlate(plate).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Vehicle with plate '%s' was not found.", plate));
		});
	}

	@Transactional
	public VehicleResponse createVehicle(VehicleRequest request) {
		if (repository.existsByPlate(request.plate())) {
			throw new RuntimeException(
					String.format("A vehicle with plate '%s' already exists.", request.plate()));
		}
		var vehicle = mapper.build(request);
		repository.save(vehicle);

		eventPublisher.publishEvent(new VehicleCreatedEvent(vehicle));
		return mapper.toResponse(vehicle);
	}

	@Transactional
	public void deleteVehicleByPlate(String plate) {
		if (!repository.existsByPlate(plate)) {
			throw new RuntimeException(String.format("Vehicle with plate '%s' does not exist.", plate));
		}
		eventPublisher.publishEvent(new VehicleDeletedEvent(plate));
		repository.deleteByPlate(plate);
	}

}
