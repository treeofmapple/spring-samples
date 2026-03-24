package com.tom.first.username.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.username.dto.UsernameRequest;
import com.tom.first.username.dto.UsernameResponse;
import com.tom.first.username.model.Username;

@Service
public class UsernameMapper {

	public Username toUsername(UsernameRequest request) {
		if (request == null) {
			return null;
		}

		return Username.builder().name(request.name()).password(request.password()).email(request.email()).build();
	}

	public UsernameResponse fromUsername(Username username) {
		if (username == null) {
			return null;
		}

		return new UsernameResponse(username.getId(), username.getName(), username.getPassword(), username.getEmail());
	}
	
	public void mergeData(Username username, UsernameRequest request) {
		username.setName(request.name());
		username.setPassword(request.password());
		username.setEmail(request.email());
	}

}
