package com.tom.mail.sender.global.system;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfigInfo {

	/*
	
	@Value("${storage.allowed-types}")
    private List<String> allowedTypes;
	
    @Value("${application.size.file:20}")
    private long fileSizeMb;

    @Value("${cache.chances.max-attempts:5}")
    private int maxLoginAttempts;

    @Value("${cache.time.login-attempt:15m}")
    private String loginCacheTime;

    @Value("${spring.servlet.multipart.max-file-size:50MB}")
    private String serverMaxFileSize;

    @Value("${spring.servlet.multipart.max-request-size:50MB}")
    private String serverMaxRequestSize;
	
    public SystemInfo fetchSystemInformations() {
        return new SystemInfo(
            allowedTypes,
            fileSizeMb,
            maxLoginAttempts,
            loginCacheTime,
            serverMaxFileSize,
            serverMaxRequestSize,
            PostConstraints.MAX_TITLE_LENGTH,
            PostConstraints.MAX_CONTENT_LENGTH
        );
    }
	*/
}
