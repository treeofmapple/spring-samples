package com.tom.first.vehicle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tom.first.vehicle.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Optional<Vehicle> findByPlate(String plate);
	
	boolean existsByPlate(String plate);

	void deleteByPlate(String plate);
	
}
