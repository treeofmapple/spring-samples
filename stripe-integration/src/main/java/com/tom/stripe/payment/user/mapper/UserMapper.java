package com.tom.stripe.payment.user.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.stripe.payment.user.dto.PageUserResponse;
import com.tom.stripe.payment.user.dto.PostalInfoRequest;
import com.tom.stripe.payment.user.dto.SimpleUserResponse;
import com.tom.stripe.payment.user.dto.UserRequest;
import com.tom.stripe.payment.user.dto.UserResponse;
import com.tom.stripe.payment.user.dto.UserUpdate;
import com.tom.stripe.payment.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	User build(UserRequest request);
	
	UserResponse toResponse(User user);

	SimpleUserResponse toSimpleResponse(User user);

	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget User user, PostalInfoRequest request);
	
	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget User user, UserUpdate request);

	List<SimpleUserResponse> toResponseList(List<User> users);

	default PageUserResponse toResponse(Page<User> page) {
		if (page == null) {
			return null;
		}
		List<SimpleUserResponse> content = toResponseList(page.getContent());
		return new PageUserResponse(content, page.getNumber(), page.getSize(), page.getTotalPages());
	}
}
