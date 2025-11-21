package com.tom.first.elastic.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.mapper.VehicleMapper;
import com.tom.first.elastic.repository.VehicleSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleSearchRepository repository;
	private final VehicleMapper mapper;

	public VehicleResponse findByPlate(String plate) {
		return repository.findByPlate(plate).map(mapper::toResponse)
				.orElseThrow(() -> new RuntimeException(String.format("Vehicle with plate '%s' not found", plate)));
	}

	public List<VehicleResponse> findByBrand(String brand) {
		return repository.findByBrand(brand).stream().map(mapper::toResponse).toList();
	}

	public List<VehicleResponse> findByModel(String model) {
		return repository.findByModel(model).stream().map(mapper::toResponse).toList();
	}

	public List<VehicleResponse> findByColor(String color) {
		return repository.findByColor(color).stream().map(mapper::toResponse).toList();
	}

	public List<VehicleResponse> findByCreatedAtRange(ZonedDateTime from, ZonedDateTime to) {
		return repository.findByCreatedAtBetween(from, to).stream().map(mapper::toResponse).toList();
	}
	
}

