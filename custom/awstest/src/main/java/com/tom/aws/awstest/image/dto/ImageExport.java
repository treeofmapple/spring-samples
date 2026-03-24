package com.tom.aws.awstest.image.dto;

import org.springframework.core.io.Resource;

public record ImageExport(
		
		Resource resource,
		String fileName,
		int fileSize
		
		) {

}
