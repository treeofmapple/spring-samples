package com.tom.sample.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.tom.sample.auth.dto.AuthenticationResponse;
import com.tom.sample.auth.dto.UserResponse;
import com.tom.sample.auth.model.Token;
import com.tom.sample.auth.model.User;
import com.tom.sample.auth.model.enums.TokenType;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenticationMapper {
	AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);
	
	// @Mapping(source = "", target = "")
	
	@Mapping(source = "name", target = "name")
	@Mapping(source = "username", target = "username")
	@Mapping(source = "age", target = "age")
	@Mapping(source = "email", target = "email")
	@Mapping(source = "password", target = "password")
	@Mapping(source = "emailVerified", target = "emailVerified")
	@Mapping(source = "verificationToken", target = "verificationToken")
	User buildAttributes(String name, String username, int age, String email, String password, boolean emailVerified, String verificationToken);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "user", target = "user")
	@Mapping(source = "token", target = "token")
	@Mapping(source = "tokenType", target = "tokenType")
	@Mapping(source = "revoked", target = "revoked")
	@Mapping(source = "expired", target = "expired")
	Token buildAttributes(User user, String token, TokenType tokenType, boolean revoked, boolean expired);
	
	@Mapping(source = "name", target = "name")
	@Mapping(source = "username", target = "username")
	@Mapping(source = "email", target = "email")
	UserResponse buildUserResponse(User user);
	
	@Mapping(source = "jwtToken", target = "accessToken")
	@Mapping(source = "refreshToken", target = "refreshToken")
	AuthenticationResponse buildResponse(String jwtToken, String refreshToken);
	
}
