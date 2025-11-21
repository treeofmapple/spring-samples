package com.tom.first.vehicle.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.vehicle.model.VehicleDocument;

import jakarta.transaction.Transactional;

@Repository
public interface VehicleSearchRepository extends ElasticsearchRepository<VehicleDocument, String> {

    boolean existsByPlate(String plate);
	
    @Modifying
    @Transactional
    void deleteByPlate(String plate);
	
}
