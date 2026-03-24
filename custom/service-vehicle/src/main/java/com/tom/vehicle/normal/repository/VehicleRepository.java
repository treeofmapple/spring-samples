package com.tom.vehicle.normal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tom.vehicle.normal.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	Optional<Vehicle> findByPlate(String plate);

	boolean existsByPlate(String plate);

	@Query("""
			SELECT v FROM Vehicle v
			WHERE v.id <> :id
			  AND (
			        (:plate IS NOT NULL AND v.plate = :plate)
			  )
			""")
	boolean existsConflict(@Param("id") Long id, @Param("plate") String plate);

}
