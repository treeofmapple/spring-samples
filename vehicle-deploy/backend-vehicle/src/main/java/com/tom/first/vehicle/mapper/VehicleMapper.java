package com.tom.first.vehicle.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.first.vehicle.dto.BrandResponse;
import com.tom.first.vehicle.dto.ModelResponse;
import com.tom.first.vehicle.dto.PageVehicleResponse;
import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.dto.VehicleUpdate;
import com.tom.first.vehicle.model.Brand;
import com.tom.first.vehicle.model.Model;
import com.tom.first.vehicle.model.Vehicle;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "brand", ignore = true)
	@Mapping(target = "model", ignore = true)
	@Mapping(target = "type", ignore = true)
	@Mapping(source = "licensePlate", target = "plate")
	Vehicle build(VehicleRequest request);
	
	@Mapping(source = "name", target = "name")
	Model buildModel(String name);

	@Mapping(source = "name", target = "name")
	Brand buildBrand(String name);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "brand", ignore = true)
	@Mapping(target = "model", ignore = true)
	@Mapping(source = "licensePlate", target = "plate")
	Vehicle update(@MappingTarget Vehicle vehicle, VehicleUpdate request);
	
	@Mapping(source = "plate", target = "licensePlate")
	@Mapping(source = "brand.name", target = "brandName")
	@Mapping(source = "model.name", target = "modelName")	
	VehicleResponse toResponse(Vehicle vehicle); 

	@Mapping(source = "brand.name", target = "brandName")
	ModelResponse toResponse(Model model);
	
	BrandResponse toResponse(Brand brand);
	
	List<VehicleResponse> toResponseList(List<Vehicle> vehicles);
	
	default PageVehicleResponse toResponse(Page<Vehicle> page) {
		if(page == null) {
			return null;
		}
		
		List<VehicleResponse> content = toResponseList(page.getContent());
		return new PageVehicleResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
				);
	}
	
}
