package com.tom.first.library.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tom.first.library.dto.UserRequest;
import com.tom.first.library.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
    private final WebClient userServiceClient;

    public UserResponse getUserById(Long id) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("id", id).build())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse getUserByUsername(String username) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("username", username).build())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
    
    public UserResponse createUser(UserRequest request) {
        return userServiceClient.post()
                .uri("")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
	
}
