package com.tom.first.elastic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.models.vehicle.VehicleDocument;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	VehicleResponse toResponse(VehicleDocument vehicle);

}
