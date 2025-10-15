package com.tom.auth.monolithic.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoginAttemptService {

    @Value("${application.login.max.attempts:5}")
    private int MAX_ATTEMPTS;

    @Value("${application.login.user.creation:3}")
    private int MAX_USER_CREATION;
    
    private final CacheManager cacheManager;
    private Cache attemptsCache;
    private Cache registerCache;

    public LoginAttemptService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
	
    @PostConstruct
    public void init() {
        this.attemptsCache = cacheManager.getCache("login-attempt");
        this.registerCache = cacheManager.getCache("register-attempt");
        if (this.attemptsCache == null) {
            throw new IllegalStateException("Cache 'login-attempt' not found. Please check your cache configuration.");
        }
        
        if (this.registerCache == null) {
            throw new IllegalStateException("Cache 'register-attempt' not found. Please check your cache configuration.");
        }
    }

    public void loginSucceeded(String key) {
        attemptsCache.evict(key);
    }

    public void loginFailed(String key) {
        Integer attempts = attemptsCache.get(key, Integer.class);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public void isLoginBlocked(String key) {
        Integer attempts = attemptsCache.get(key, Integer.class);
        if(attempts != null && attempts >= MAX_ATTEMPTS) {
        	log.warn("Blocked login attempt for user: {}", key);
	        throw new LockedException("User account is locked due to too many failed login attempts.");
        }
    }
    
    public void incrementUserRegistrationLimit(String ip) {
        Integer attempts = registerCache.get(ip, Integer.class);
        int newCount = (attempts == null) ? 1 : attempts + 1;
        registerCache.put(ip, newCount);
    }
    
    public void userRegistrationBlocked(String ip) {
    	Integer limit = registerCache.get(ip, Integer.class);
    	if(limit != null && limit >= MAX_USER_CREATION) {
    		log.warn("Blocked registration attempt from IP: {}. Too many accounts created.");
            throw new IllegalStateException("You have created too many accounts. Please try again later.");
    	}
    }
    
}
