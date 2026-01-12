package com.tom.vehicle.normal.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.vehicle.normal.dto.PageVehicleResponse;
import com.tom.vehicle.normal.dto.VehicleRequest;
import com.tom.vehicle.normal.dto.VehicleResponse;
import com.tom.vehicle.normal.dto.VehicleUpdate;
import com.tom.vehicle.normal.model.Vehicle;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	Vehicle build(VehicleRequest request);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	Vehicle build(@MappingTarget Vehicle vehicle, VehicleUpdate request);
	
	VehicleResponse toResponse(Vehicle vehicle);

	List<VehicleResponse> toResponseList(List<Vehicle> vehicles);

	default PageVehicleResponse toResponse(Page<Vehicle> page) {
		if (page == null) {
			return null;
		}
		
		List<VehicleResponse> content = toResponseList(page.getContent());
		return new PageVehicleResponse(content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages()
				);
	}

}
