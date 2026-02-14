package com.tom.mail.sender.global.system;

public record SystemInfo(
		
		long pageSize,
		int maxTitleLength,
		int maxContentLength,
		int mailBatchSize,
		int mailWaitTime,
		String templateFileExtension,
		String variablesFileExtension
		
) {

}
