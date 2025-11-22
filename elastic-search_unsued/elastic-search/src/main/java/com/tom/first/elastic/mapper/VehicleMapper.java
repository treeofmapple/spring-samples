package com.tom.first.elastic.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.dto.paged.PageVehicleResponse;
import com.tom.first.elastic.models.vehicle.VehicleDocument;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

	VehicleResponse toResponse(VehicleDocument vehicle);

	List<VehicleResponse> toResponseList(List<VehicleDocument> vehicles);

	default PageVehicleResponse toResponse(Page<VehicleDocument> page) {
		if (page == null) {
			return null;
		}
		
		List<VehicleResponse> content = toResponseList(page.getContent());
		return new PageVehicleResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalElements()
				); 
		
	}

}
