package com.tom.first.vehicle.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.vehicle.mapper.VehicleMapper;
import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.repository.VehicleRepository;
import com.tom.first.vehicle.request.VehicleRequest;
import com.tom.first.vehicle.request.VehicleResponse;
import com.tom.first.vehicle.request.VehicleUpdate;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository repository;
	private final VehicleMapper mapper;

	public List<VehicleResponse> findAll() {
		List<Vehicle> vehicles = repository.findAll();
		if (vehicles.isEmpty()) {
			throw new RuntimeException("No vehicles were found.");
		}
		return vehicles.stream().map(mapper::fromVehicle).collect(Collectors.toList());
	}

	public VehicleResponse findByPlate(String plate) {
		return repository.findByPlate(plate)
				.map(mapper::fromVehicle)
				.orElseThrow(() -> {
					return new RuntimeException(String.format("Vehicle with plate '%s' was not found.", plate));
				});
	}

	@Transactional
	public VehicleResponse createVehicle(VehicleRequest request) {
		if (repository.existsByPlate(request.licensePlate())) {
			throw new RuntimeException(String.format("A vehicle with plate '%s' already exists.", request.licensePlate()));
		}

		var vehicle = mapper.toVehicle(request);
		repository.save(vehicle);
		return mapper.fromVehicle(vehicle);
	}

	@Transactional
	public VehicleResponse updateVehicle(String plate, VehicleUpdate request) {

		var vehicle = repository.findByPlate(plate)
				.orElseThrow(() -> {
					return new RuntimeException(String.format("Vehicle with plate '%s' was not found.", plate));
				});

		mapper.mergeVehicle(vehicle, request);
		repository.save(vehicle);
		return mapper.fromVehicle(vehicle);
	}

	@Transactional
	public void deleteVehicle(String plate) {

		if (!repository.existsByPlate(plate)) {
			throw new RuntimeException(String.format("Vehicle with plate '%s' does not exist.", plate));
		}

		repository.deleteByPlate(plate);
	}
}
