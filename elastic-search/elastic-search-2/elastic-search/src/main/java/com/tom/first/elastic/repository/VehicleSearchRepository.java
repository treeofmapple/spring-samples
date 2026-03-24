package com.tom.first.elastic.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.vehicle.VehicleDocument;

@Repository
public interface VehicleSearchRepository extends ElasticsearchRepository<VehicleDocument, String> {

	Optional<VehicleDocument> findByPlate(String plate);

	// List<VehicleDocument> findByType(Type type);
	// List<VehicleDocument> findByCreatedAtBetween(ZonedDateTime start, ZonedDateTime end);
	
}
