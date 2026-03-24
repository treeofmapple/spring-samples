package com.tom.vehicle.dynamodb.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tom.vehicle.dynamodb.model.Vehicle;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
@RequiredArgsConstructor
public class VehicleRepository {

	private final DynamoDbTable<Vehicle> table;

	public Optional<Vehicle> findById(Long id) {
		Vehicle v = table.getItem(Key.builder().partitionValue(id).build());
		return Optional.ofNullable(v);
	}

	public Optional<Vehicle> findByPlate(String plate) {
		var index = table.index("plate-index");

		var results = index.query(
				r -> r.queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(plate).build())));

		return results.stream().flatMap(p -> p.items().stream()).findFirst();
	}

	public boolean existsByPlate(String plate) {
		return findByPlate(plate).isPresent();
	}

	public void deleteByPlate(String plate) {
		findByPlate(plate).ifPresent(v -> table.deleteItem(Key.builder().partitionValue(v.getId()).build()));
	}

	public void save(Vehicle vehicle) {
		table.putItem(vehicle);
	}

}
