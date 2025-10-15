package com.tom.aws.lambda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record EmployeeResponse(

		String id,
		String name,
		String email,
		String jobTitle,
		String phone,
		String imageUrl,
		String employeeCode
		
		) {

}
