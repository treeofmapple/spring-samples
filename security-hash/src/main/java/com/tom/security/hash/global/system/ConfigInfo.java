package com.tom.security.hash.global.system;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tom.security.hash.global.constraints.UserConstraints;

@Component
public class ConfigInfo extends UserConstraints {

	private ConfigInfo() {
		super();
	}
	
    @Value("${cache.chances.max-attempts:5}")
    private Integer maxLoginAttempts;

    @Value("${cache.time.login-attempt:15m}")
    private Duration loginAttemptTimeoutTime;

	@Value("${security.https-only:false}")
	private Boolean httpsOnly;
	
	@Value("${application.size.page:20}")
	private Integer applicationPageSizeDefault;
	
    public SystemInfo returnSystemBuilded() {
        return new SystemInfo(
            maxLoginAttempts,
            loginAttemptTimeoutTime,
            httpsOnly,
            applicationPageSizeDefault,
            NICKNAME_MIN_LENGTH,
            NICKNAME_MAX_LENGTH,
            EMAIL_MIN_LENGTH,
            EMAIL_MAX_LENGTH,
            MINIMAL_PASSWORD_SIZE
        );
    }
}
