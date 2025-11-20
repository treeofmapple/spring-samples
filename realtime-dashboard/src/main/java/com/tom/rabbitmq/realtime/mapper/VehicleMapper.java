package com.tom.rabbitmq.realtime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.rabbitmq.realtime.dto.VehicleRequest;
import com.tom.rabbitmq.realtime.dto.VehicleResponse;
import com.tom.rabbitmq.realtime.dto.VehicleTelemetryResponse;
import com.tom.rabbitmq.realtime.model.Vehicle;
import com.tom.rabbitmq.realtime.processes.events.VehicleTelemetryMessage;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleRequest request);

	VehicleResponse toResponse(Vehicle vehicle);

	VehicleTelemetryMessage buildTelemetry(Vehicle Vehicle);

	VehicleTelemetryResponse toResponseTelemetry(Vehicle Vehicle);

}
