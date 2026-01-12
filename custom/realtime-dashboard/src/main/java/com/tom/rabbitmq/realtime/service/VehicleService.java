package com.tom.rabbitmq.realtime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.rabbitmq.realtime.dto.VehicleRequest;
import com.tom.rabbitmq.realtime.dto.VehicleResponse;
import com.tom.rabbitmq.realtime.mapper.VehicleMapper;
import com.tom.rabbitmq.realtime.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;
	private final VehicleMapper mapper;

	@Transactional(readOnly = true)
	public VehicleResponse findById(long query) {
		return vehicleRepository.findById(query).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Vehicle with id '%s' was not found.", query));
		});
	}

	@Transactional(readOnly = true)
	public VehicleResponse findByPlateNumber(String plateNumber) {
		return vehicleRepository.findByPlateNumber(plateNumber).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("Vehicle with plate number: %s was not found.", plateNumber));
		});
	}

	@Transactional
	public VehicleResponse createVehicle(VehicleRequest request) {
		if (vehicleRepository.existsByPlateNumber(request.plateNumber())) {
			throw new RuntimeException(
					String.format("A vehicle with the same plate number already exists %s", request.plateNumber()));
		}
		var vehicle = mapper.build(request);
		vehicleRepository.save(vehicle);
		return mapper.toResponse(vehicle);
	}

	@Transactional
	public void deleteVehicleById(Long query) {
		if (!vehicleRepository.existsById(query)) {
			throw new RuntimeException(String.format("No vehicle was found with the Id: %s.", query));
		}
		vehicleRepository.deleteById(query);
	}

	@Transactional
	public void deleteVehicleByPlateNumber(String plateNumber) {
		var vehicle = vehicleRepository.findByPlateNumber(plateNumber).orElseThrow(() -> new RuntimeException(
				String.format("Vehicle with the number plate was not found: %s", plateNumber)));

		vehicleRepository.deleteById(vehicle.getId());
	}

}
