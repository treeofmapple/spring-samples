package com.tom.front.full.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record EmployeeResponse(

		Long id,
		String name,
		String email,
		String jobTitle,
		String phone,
		String imageUrl,
		String employeeCode
		
		) {

}
