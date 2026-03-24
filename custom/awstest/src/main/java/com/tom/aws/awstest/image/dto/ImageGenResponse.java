package com.tom.aws.awstest.image.dto;

public record ImageGenResponse(
		
		byte[] content,
		String fileName,
		String contentType
		
		) {

}
