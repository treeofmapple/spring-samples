package com.tom.first.vehicle.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.first.vehicle.dto.VehicleOutboxBuild;
import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.model.VehicleDocument;
import com.tom.first.vehicle.model.VehicleOutbox;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleRequest request);

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleDocument doc);

	@Mapping(target = "id", ignore = true)
	VehicleDocument build(Vehicle vehicle);
	
	VehicleOutbox build(VehicleOutboxBuild outboxBuild);

	VehicleResponse toResponse(Vehicle vehicle);

}
