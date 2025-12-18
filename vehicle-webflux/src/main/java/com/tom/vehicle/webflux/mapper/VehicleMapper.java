package com.tom.vehicle.webflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.vehicle.webflux.dto.VehicleRequest;
import com.tom.vehicle.webflux.dto.VehicleResponse;
import com.tom.vehicle.webflux.model.Vehicle;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleRequest request);

	VehicleResponse toResponse(Vehicle vehicle);

}
