package com.tom.front.full.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.front.full.dto.EmployeeRequest;
import com.tom.front.full.dto.EmployeeResponse;
import com.tom.front.full.dto.EmployeeUpdate;
import com.tom.front.full.dto.PageEmployeeResponse;
import com.tom.front.full.model.Employee;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

	@Mapping(target = "id", ignore = true)
	Employee build(EmployeeRequest request);

	@Mapping(target = "id", ignore = true)
	Employee update(@MappingTarget Employee employee, EmployeeUpdate request);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	EmployeeResponse toResponse(Employee employee);

	List<EmployeeResponse> toResponseList(List<Employee> employee);
	
	default PageEmployeeResponse toResponse(Page<Employee> page) {
		if 	(page == null) {
			return null;
		}
		List<EmployeeResponse> content = toResponseList(page.getContent());
		return new PageEmployeeResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
				);
	}
	
}
