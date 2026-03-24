package com.tom.vehicle.normal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.vehicle.normal.dto.PageVehicleResponse;
import com.tom.vehicle.normal.dto.VehicleRequest;
import com.tom.vehicle.normal.dto.VehicleResponse;
import com.tom.vehicle.normal.dto.VehicleUpdate;
import com.tom.vehicle.normal.mapper.VehicleMapper;
import com.tom.vehicle.normal.model.Vehicle;
import com.tom.vehicle.normal.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final VehicleRepository repository;
	private final VehicleMapper mapper;

	@Transactional(readOnly = true)
	public PageVehicleResponse findByPages(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Vehicle> vehicle = repository.findAll(pageable);
		return mapper.toResponse(vehicle);
	}

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
			throw new RuntimeException(String.format("A vehicle with plate '%s' already exists.", request.plate()));
		}
		var vehicle = mapper.build(request);
		repository.save(vehicle);

		return mapper.toResponse(vehicle);
	}

	@Transactional
	public VehicleResponse updateVehicle(long query, VehicleUpdate request) {
		var olderVehicle = repository.findById(query).orElseThrow(() -> new RuntimeException("Vehicle was not found"));

		if (request.plate() != null && repository.existsConflict(query, request.plate())) {
			throw new RuntimeException("Plate already in use");
		}

		var vehicle = mapper.build(olderVehicle, request);
		repository.save(vehicle);
		return mapper.toResponse(vehicle);
	}
	
	@Transactional
	public void deleteVehicleByPlate(String plate) {
		var vehicle = repository.findByPlate(plate).orElseThrow(
				() -> new RuntimeException(String.format("Vehicle with plate '%s' does not exist.", plate)));
		repository.deleteById(vehicle.getId());
	}

}
