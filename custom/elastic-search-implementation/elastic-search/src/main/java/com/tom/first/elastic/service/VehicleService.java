package com.tom.first.elastic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.dto.paged.PageVehicleResponse;
import com.tom.first.elastic.mapper.VehicleMapper;
import com.tom.first.elastic.models.vehicle.VehicleDocument;
import com.tom.first.elastic.repository.VehicleSearchRepository;
import com.tom.first.elastic.service.search.VehicleSearchUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final VehicleSearchRepository repository;
	private final VehicleMapper mapper;
	private final VehicleSearchUtil searchUtil;

	@Transactional(readOnly = true)
	public PageVehicleResponse searchVehicleByParams(int page, String plate, String brand, String model, String color) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<VehicleDocument> vehicle = searchUtil.searchByCriteria(plate, brand, model, color, pageable);
		return mapper.toResponse(vehicle);
	}

	@Transactional(readOnly = true)
	public VehicleResponse findByPlate(String plate) {
		return repository.findByPlate(plate).map(mapper::toResponse)
				.orElseThrow(() -> new RuntimeException(String.format("Vehicle with plate '%s' not found", plate)));
	}

}
