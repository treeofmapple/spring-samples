package com.tom.security.hash.logic.caching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class LoginAttemptComponent {

	@Value("${cache.chances.max-attempts:5}")
	private int MAX_ATTEMPTS;

	@Value("${cache.name.login-attempt}")
	private String cacheLoginAttemptName;

	private final HazelcastInstance hazelcastInstance;
	private IMap<String, Long> attemptsMap;

	@PostConstruct
	public void init() {
		this.attemptsMap = hazelcastInstance.getMap(cacheLoginAttemptName);
	}

	public void loginSucceeded(String key) {
		attemptsMap.remove(key);
	}

	public void loginFailed(String key) {
		attemptsMap.executeOnKey(key, entry -> {
			Long current = entry.getValue();
			if (current == null) {
				entry.setValue(1L);
			} else {
				entry.setValue(current + 1);
			}
			return null;
		});
		log.info("Login failed for {}. Current attempts recorded in cluster.", key);
	}

	public void isLoginBlocked(String key) {
		Long attempts = attemptsMap.get(key);
		if (attempts != null && attempts >= MAX_ATTEMPTS) {
			log.warn("Blocked login attempt for user: {}. Attempts: {}", key, attempts);
			throw new LockedException("User account is locked due to too many failed login attempts.");
		}
	}

}
