package com.tom.first.simple.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tom.first.simple.dto.UserRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	UserRequest build(String username, String password, String email);
	
}
