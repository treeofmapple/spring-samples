package com.tom.security.hash.global.system;

import java.time.Duration;

public record SystemInfo(
		
		Integer maxLoginAttempts,
		Duration loginAttemptTimeoutTime,
		Boolean httpsOnly,
		Integer applicationPageSizeDefault,
		Integer nicknameMinLength,
		Integer nicknameMaxLength,
		Integer emailMinLength,
		Integer emailMaxLength,
		Integer minimalPasswordSize
		
) {

}
