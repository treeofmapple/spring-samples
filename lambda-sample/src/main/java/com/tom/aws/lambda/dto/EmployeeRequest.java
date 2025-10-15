package com.tom.aws.lambda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
		
		@NotBlank(message = "Requires Employee Name")
		@Size(min = 2, max = 255, message = "Name is passing the limit")
		String name,
		
		@NotBlank(message = "Requires Employee Email")
		@Size(min = 2, max = 255, message = "Email is passing the size limit")
		String email,
		
		@NotBlank(message = "Requires Employee Job title")
		@Size(min = 2, max = 100, message = "Employee Job Title is passing the limit")
		String jobTitle,
		
		@Size(min = 2, max = 40, message = "Phone is passing the size limit")
		String phone,
		
		@Size(min = 2, max = 1024, message = "ImageUrl is passing the size limit")
		String imageUrl
		
		) {

}
