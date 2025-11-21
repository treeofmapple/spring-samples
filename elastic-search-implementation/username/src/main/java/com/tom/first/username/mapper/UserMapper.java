package com.tom.first.username.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.first.username.dto.UserOutboxBuild;
import com.tom.first.username.dto.UserRequest;
import com.tom.first.username.dto.UserResponse;
import com.tom.first.username.model.User;
import com.tom.first.username.model.UserDocument;
import com.tom.first.username.model.UserOutbox;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	User build(UserRequest request);
	
	@Mapping(target = "id", ignore = true)
	User build(UserDocument doc);
	
	@Mapping(target = "id", ignore = true)
	UserDocument build(User username);
	
	UserOutbox build(UserOutboxBuild outboxBuild);
	
	UserResponse toResponse(User username);
	
}
