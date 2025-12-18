package com.tom.vehicle.dynamodb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.vehicle.dynamodb.dto.VehicleRequest;
import com.tom.vehicle.dynamodb.dto.VehicleResponse;
import com.tom.vehicle.dynamodb.model.Vehicle;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleRequest request);

	VehicleResponse toResponse(Vehicle vehicle);

}
