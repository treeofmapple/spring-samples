package com.tom.first.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tom.first.library.dto.UserRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	UserRequest build(String username, String password, String email);
	
}
