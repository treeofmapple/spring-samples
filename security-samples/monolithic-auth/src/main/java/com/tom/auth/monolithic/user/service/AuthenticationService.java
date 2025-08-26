package com.tom.auth.monolithic.user.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.auth.monolithic.exception.InvalidTokenException;
import com.tom.auth.monolithic.security.JwtService;
import com.tom.auth.monolithic.security.LoginAttemptService;
import com.tom.auth.monolithic.security.SecurityUtils;
import com.tom.auth.monolithic.user.dto.authentication.AuthenticationRequest;
import com.tom.auth.monolithic.user.dto.authentication.AuthenticationResponse;
import com.tom.auth.monolithic.user.dto.user.RegisterRequest;
import com.tom.auth.monolithic.user.mapper.UserMapper;
import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.model.enums.Role;
import com.tom.auth.monolithic.user.repository.TokenRepository;
import com.tom.auth.monolithic.user.repository.UserRepository;
import com.tom.auth.monolithic.user.service.utils.CookiesUtils;
import com.tom.auth.monolithic.user.service.utils.LoginHistoryUtils;
import com.tom.auth.monolithic.user.service.utils.TokenUtils;
import com.tom.auth.monolithic.user.service.utils.UserUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${application.security.cookie-name}")
    private String refreshTokenCookieName;
    
    @Value("${application.security.refresh-token}")
    private Duration refreshExpiration;
	
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final UserMapper userMapper;
	private final UserUtils userUtils;
	private final TokenUtils tokenUtils;
	private final CookiesUtils cookiesUtils;
	private final SecurityUtils securityUtils;
	private final LoginHistoryUtils loginUtils;
	private final JwtService jwtService;
	private final LoginAttemptService loginTryService;
	
	
	@Transactional
	public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
		var userIp = securityUtils.getRequestingClientIp();
		log.info("IP: {}, is creating user.", userIp);
	
		loginTryService.userRegistrationBlocked(userIp);
		
		userUtils.ensureUsernameAndEmailAreUnique(request.username(), request.email());
		
		if (!request.password().equals(request.confirmPassword())) {
			throw new BadCredentialsException("Passwords not matches");
		}

		var user = userMapper.build(request);
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setRole(Role.USER);
        user.setAccountNonLocked(true);
		user.setEnabled(true);
		var savedUser = userRepository.save(user);
		loginUtils.storeLoginHistory(savedUser, userIp);
		
		var jwtToken = jwtService.generateToken(savedUser);
		var refreshToken = jwtService.generateRefreshToken(savedUser);
		
        tokenUtils.saveUserToken(savedUser, refreshToken);

        loginTryService.incrementUserRegistrationLimit(userIp);
        
		cookiesUtils.clearCookie(response, refreshTokenCookieName);
		cookiesUtils.addCookie(response, refreshTokenCookieName, refreshToken, refreshExpiration);
		log.info("User registered: | Username: {}, | Email: {}", savedUser.getUsername(), savedUser.getEmail());
		return userMapper.toResponse(jwtToken, refreshToken);
	}

	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
		String identifier = request.userinfo();
		var userIp = securityUtils.getRequestingClientIp();
		log.info("IP: {}, is authenticating user: {}", userIp , identifier);
		
	    loginTryService.isLoginBlocked(identifier);
	    cookiesUtils.checkIfUserIsAlreadyAuthenticated(identifier, httpRequest, refreshTokenCookieName);
	    
		try {
			var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

			loginTryService.loginSucceeded(identifier);
			
			var user = (User) auth.getPrincipal();
			var jwtToken = jwtService.generateToken(user);
			var refreshToken = jwtService.generateRefreshToken(user);
			
			tokenUtils.saveUserToken(user, refreshToken);
			loginUtils.storeLoginHistory(user, userIp);
			
			cookiesUtils.clearCookie(response, refreshTokenCookieName);
			cookiesUtils.addCookie(response, refreshTokenCookieName, refreshToken, refreshExpiration);
			log.info("IP: {}, the user: {}, has been authenticated", securityUtils.getRequestingClientIp(), user.getUsername());
			return userMapper.toResponse(jwtToken, refreshToken);
		} catch (BadCredentialsException e) {
	        log.warn("Failed login attempt for user: {}", identifier);
	        loginTryService.loginFailed(identifier);
			throw e;
		}
	}
	
	@Transactional
	public AuthenticationResponse authenticateAdmin(AuthenticationRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {
		String identifier = request.userinfo();
		var userIp = securityUtils.getRequestingClientIp();
		log.info("IP: {}, is authenticating user: {}", userIp , identifier);
		
	    cookiesUtils.checkIfUserIsAlreadyAuthenticated(identifier, httpRequest, refreshTokenCookieName);
	    
		try {
			var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, request.password()));

			var user = (User) auth.getPrincipal();
			
            if (user.getRole() != Role.ADMIN) {
                log.warn("SECURITY: Non-admin user '{}' attempted to use the admin login endpoint.", identifier);
                throw new BadCredentialsException("Invalid Access");
            }
            
			var jwtToken = jwtService.generateToken(user);
			var refreshToken = jwtService.generateRefreshToken(user);
			
			tokenUtils.saveUserToken(user, refreshToken);
			loginUtils.storeLoginHistory(user, userIp);
			
			cookiesUtils.clearCookie(response, refreshTokenCookieName);
			cookiesUtils.addCookie(response, refreshTokenCookieName, refreshToken, refreshExpiration);
			log.info("IP: {}, the user: {}, has been authenticated", securityUtils.getRequestingClientIp(), user.getUsername());
			return userMapper.toResponse(jwtToken, refreshToken);
		} catch (BadCredentialsException e) {
	        log.warn("Failed login attempt for user: {}", identifier);
			throw e;
		}
	}
	
	@Transactional
	public AuthenticationResponse refreshToken(String providedRefreshToken, HttpServletRequest request, HttpServletResponse response)  {
		
        var storedToken = tokenRepository.findByToken(providedRefreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));

        if (storedToken.isRevoked()) {
            log.warn("SECURITY ALERT: Attempted re-use of revoked refresh token for user {}. Revoking all tokens for this user.", storedToken.getUser().getUsername());
            tokenUtils.revokeUserAllTokens(storedToken.getUser()); 
            
            throw new InvalidTokenException("Refresh token has been compromised.");
        }

        if (storedToken.isExpired()) {
            throw new InvalidTokenException("Refresh token is expired.");
        }

        var user = storedToken.getUser();
        
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            log.warn("User banned: {}, tried to authenticate on: {}", user.getUsername(), securityUtils.getRequestingClientIp());
            throw new InvalidTokenException("User account is disabled or locked.");
        }
        
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        tokenUtils.saveUserToken(user, newRefreshToken);

        cookiesUtils.clearCookie(response, refreshTokenCookieName);
        cookiesUtils.addCookie(response, refreshTokenCookieName, newRefreshToken, refreshExpiration);
        
        return userMapper.toResponse(newAccessToken, newRefreshToken);

	}
	
}
