package com.tom.first.vehicle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.vehicle.model.VehicleOutbox;

@Repository
public interface VehicleOutboxRepository extends JpaRepository<VehicleOutbox, Long> {

	List<VehicleOutbox> findByProcessedFalse();
}
