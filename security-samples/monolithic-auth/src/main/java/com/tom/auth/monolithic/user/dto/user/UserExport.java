package com.tom.auth.monolithic.user.dto.user;

import org.springframework.core.io.Resource;

public record UserExport(
		
		Resource resource,
		String fileName,
		int fileSize
		
		) {

}
