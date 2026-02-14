package com.tom.reactive.first.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.reactive.first.dto.PageUserResponse;
import com.tom.reactive.first.dto.UserRequest;
import com.tom.reactive.first.dto.UserResponse;
import com.tom.reactive.first.dto.UserUpdateRequest;
import com.tom.reactive.first.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { UUID.class,
		java.time.LocalDateTime.class })
public interface UserMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "active", constant = "true")
	User build(UserRequest request);

	UserResponse toResponse(User user);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget User user, UserUpdateRequest request);

	default PageUserResponse toResponse(List<User> list, Integer page, Integer size) {
		List<UserResponse> content = list.stream().map(this::toResponse).toList();
		return new PageUserResponse(content, page, size, 0, (long) list.size());
	}

}
