package com.tom.first.simple.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.simple.dto.UserRequest;
import com.tom.first.simple.dto.UserResponse;
import com.tom.first.simple.dto.user.UserGradeResponse;
import com.tom.first.simple.dto.user.UserUpdateResponse;
import com.tom.first.simple.model.User;

@Service
public class UserMapper {

	public User toUser(UserRequest request) {
		if (request == null) {
			return null;
		}

		return User.builder().name(request.name()).email(request.email()).build();
	}

	public UserResponse fromUser(User user) {
		if (user == null) {
			return null;
		}

		return new UserResponse(user.getName(), user.getEmail(), user.getEvaluations());
	}
	
	public UserGradeResponse fromUserGrade(String name, double averageGrade) {
		if (name == null) {
			return null;
		}
		return new UserGradeResponse(name, averageGrade);
	}
	
	public UserUpdateResponse fromUserUpdate(User user) {
		if (user == null) {
			return null;
		}
		return new UserUpdateResponse(user.getName(), user.getEmail());
	}

	public void mergeUser(User user, UserRequest request) {
		user.setName(request.name());
		user.setEmail(request.email());
	}
	
}
