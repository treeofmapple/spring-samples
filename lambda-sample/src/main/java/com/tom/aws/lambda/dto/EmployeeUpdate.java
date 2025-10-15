package com.tom.aws.lambda.dto;

public record EmployeeUpdate(
		
		String name,
		String email,
		String jobTitle,
		String phone,
		String imageUrl
		
		) {

}
