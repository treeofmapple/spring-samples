package com.tom.first.elastic.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.vehicle.Type;
import com.tom.first.elastic.models.vehicle.VehicleDocument;

@Repository
public interface VehicleSearchRepository extends ElasticsearchRepository<VehicleDocument, String> {

	Optional<VehicleDocument> findByPlate(String plate);

	List<VehicleDocument> findByBrand(String brand);
	List<VehicleDocument> findByModel(String model);
	List<VehicleDocument> findByColor(String color);
	List<VehicleDocument> findByType(Type type);
	List<VehicleDocument> findByCreatedAtBetween(ZonedDateTime start, ZonedDateTime end);

	
}
