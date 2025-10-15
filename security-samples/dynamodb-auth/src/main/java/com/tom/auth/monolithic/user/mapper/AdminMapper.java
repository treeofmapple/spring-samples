package com.tom.auth.monolithic.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.auth.monolithic.user.dto.admin.UserAdminResponse;
import com.tom.auth.monolithic.user.dto.admin.DeleteListResponse;
import com.tom.auth.monolithic.user.dto.admin.PageAdminUserResponse;
import com.tom.auth.monolithic.user.model.User;

@Mapper(componentModel = "spring", 
		unmappedTargetPolicy = ReportingPolicy.IGNORE, 
		uses = { MappingUtils.class }
)
public interface AdminMapper {

	@Mapping(source = "username", target = "username")
	@Mapping(source = "email", target = "email")
	@Mapping(source = "age", target = "age")
	@Mapping(source = "role", target = "role")
	@Mapping(source = "createdAt", target = "createdAt")
	@Mapping(source = "accountNonLocked", target = "accountNonLocked")
	@Mapping(source = "enabled", target = "enabled")
	@Mapping(source = "user", target = "lastLogin", qualifiedByName = "getLastLoginTime")
	UserAdminResponse toResponse(User user);
    
    default DeleteListResponse toResponse(List<UUID> ids) {
        if (ids == null) {
            return new DeleteListResponse(Collections.emptyList());
        }
        return new DeleteListResponse(ids);
    }
    
	List<UserAdminResponse> toResponseList(List<User> users);
	
	default PageAdminUserResponse toResponse(Page<User> page) {
		if(page == null) {
			return null;
		}
		List<UserAdminResponse> content = toResponseList(page.getContent());
		return new PageAdminUserResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
	}
    
}
