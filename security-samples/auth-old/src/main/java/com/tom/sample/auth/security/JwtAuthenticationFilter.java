package com.tom.sample.auth.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tom.sample.auth.common.Operations;
import com.tom.sample.auth.exception.InternalException;
import com.tom.sample.auth.repository.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;
	private final Operations operations;
	private final JwtService jwtService;
	
	@Override 
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
									@NonNull HttpServletResponse response, 
									@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		if(request.getServletPath().contains("/v1/auth")) { // Make this a way to be changeable
			filterChain.doFilter(request, response);
			return;
		}
		
	    String jwt = null;

	    final String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        jwt = authHeader.substring(7);
	    } else {
	        jwt = operations.getCookieValue(request, "access_token");
	    }

	    if (jwt == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    final String identifier = jwtService.extractUsername(jwt);
	    if (identifier == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);
	    boolean isTokenValid = tokenRepository.findByToken(jwt)
	            .map(token -> !token.isExpired() && !token.isRevoked())
	            .orElseThrow(() -> new InternalException("Token not found or revoked."));
		
	    if (!jwtService.isTokenValid(jwt, userDetails) || !isTokenValid) {
	        throw new InternalException("Invalid or expired JWT token.");
	    }

	    UsernamePasswordAuthenticationToken authToken = 
	            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    
	    SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
	
}
