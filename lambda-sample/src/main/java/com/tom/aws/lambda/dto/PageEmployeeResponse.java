package com.tom.aws.lambda.dto;

import java.util.List;

public record PageEmployeeResponse(
		
		List<EmployeeResponse> content,
		String nextPageToken
		
		) {

}
