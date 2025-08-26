package com.tom.auth.monolithic.user.service.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tom.auth.monolithic.exception.AlreadyExistsException;
import com.tom.auth.monolithic.exception.NotFoundException;
import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUtils {

	private final UserRepository repository;
	
    public User findUserByUserId(UUID userId) {
    	return repository.findById(userId)
				.orElseThrow(() -> new NotFoundException("The user id was not found"));
    }
    
    public User findUserByIdentifier(String identifier) {
    	return repository.findByIdentifier(identifier)
				.orElseThrow(() -> new NotFoundException("User username or email not found"));
    }

    public User findUserByIdOrIdentifier(String identifier) {

        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("User ID or identifier (username/email) must be provided.");
        }

        try{
            UUID userId = UUID.fromString(identifier);
            return findUserByUserId(userId);
        } catch (IllegalArgumentException e) {
        	return findUserByIdentifier(identifier);
        }
    }
    
    public void ensureUsernameAndEmailAreUnique(String username, String email) {
        if (repository.existsByUsername(username)) {
            throw new AlreadyExistsException("Username is already taken: " + username);
        }
        
        if (repository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email is already in use: " + email);
        }
    }
	
    public void checkIfEmailIsTakenByAnotherUser(User currentUser, String newEmail) {
        if (repository.existsByEmailAndIdNot(newEmail, currentUser.getId())) {
            throw new AlreadyExistsException("Email is already in use by another account: " + newEmail);
        }
    }

    public void checkIfUsernameIsTakenByAnotherUser(User currentUser, String newUsername) {
        if (repository.existsByUsernameAndIdNot(newUsername, currentUser.getId())) {
            throw new AlreadyExistsException("Username is already taken by another account: " + newUsername);
        }
    }
	
}
