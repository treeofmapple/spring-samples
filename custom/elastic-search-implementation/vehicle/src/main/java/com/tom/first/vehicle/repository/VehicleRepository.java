package com.tom.first.vehicle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.vehicle.model.Vehicle;

import jakarta.transaction.Transactional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Optional<Vehicle> findByPlate(String plate);
	
	boolean existsByPlate(String plate);

	@Modifying
    @Transactional
	void deleteByPlate(String plate);
	
}
