package com.tom.aws.awstest.image.dto;

import java.util.List;

public record PageImageResponse(
		
		List<ImageResponse> content,
		int page,
		int size,
		long totalElements
		
		) {

}
