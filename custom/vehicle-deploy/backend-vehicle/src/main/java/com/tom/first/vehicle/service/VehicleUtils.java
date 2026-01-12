package com.tom.first.vehicle.service;

import org.springframework.stereotype.Component;

import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleUtils {

	private final VehicleRepository repository;

	public Vehicle findById(long query) {
		return repository.findById(query).orElseThrow(() -> {
			throw new RuntimeException("");
		});
	}

	public void ensureArePlateUnique(String plate) {
		if (repository.existsByPlate(plate)) {
			throw new RuntimeException("Already Exists");
		}
	}

	public void checkIfPlateIsTaken(Vehicle currentVehicle, String plate) {
	    if (!currentVehicle.getPlate().equalsIgnoreCase(plate)) {
			if (repository.existsByPlateAndIdNot(plate, currentVehicle.getId())) {
				throw new RuntimeException("");
			}
	    }
	}

}
