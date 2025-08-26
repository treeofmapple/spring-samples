package com.tom.sample.auth.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OauthUserConfig {

	@Bean
	OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
	    return userRequest -> {
	        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

	        String accessToken = userRequest.getAccessToken().getTokenValue();
	        RestTemplate restTemplate = new RestTemplate();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(accessToken);
	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        ResponseEntity<Map> response = restTemplate.exchange(
	            "https://people.googleapis.com/v1/people/me?personFields=birthdays",
	            HttpMethod.GET,
	            entity,
	            Map.class
	        );

	        Map<String, Object> birthdayInfo = response.getBody();
	        System.out.println("Birthday info: " + birthdayInfo);

	        return oauth2User;
	    };
	}
	
}
