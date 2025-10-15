package com.tom.auth.monolithic.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.auth.monolithic.user.dto.authentication.AuthenticationResponse;
import com.tom.auth.monolithic.user.dto.user.DataExportationResponse;
import com.tom.auth.monolithic.user.dto.user.PageUserResponse;
import com.tom.auth.monolithic.user.dto.user.RegisterRequest;
import com.tom.auth.monolithic.user.dto.user.UpdateAccountRequest;
import com.tom.auth.monolithic.user.dto.user.UserResponse;
import com.tom.auth.monolithic.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	User build(RegisterRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	User build(UpdateAccountRequest request);

	UserResponse toResponse(User user);
	
	@Mapping(source = "jwtToken", target = "accessToken")
	@Mapping(source = "refreshToken", target = "refreshToken")
	AuthenticationResponse toResponse(String jwtToken, String refreshToken);
	
	@Mapping(target = "id", ignore = true)
	User updateUser(@MappingTarget User user, UpdateAccountRequest request);
	
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "enabled", target = "accountEnabled")
    @Mapping(source = "loginHistories", target = "loginHistory")
	DataExportationResponse dataExporation(User user);
	
	List<UserResponse> toResponseList(List<User> users);
	
	default PageUserResponse toResponse(Page<User> page) {
		if(page == null) {
			return null;
		}
		List<UserResponse> content = toResponseList(page.getContent());
		return new PageUserResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
	}
	
}
