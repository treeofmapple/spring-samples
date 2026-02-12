package com.tom.mail.sender.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.mail.sender.dto.MailRequest;
import com.tom.mail.sender.dto.MailResponse;
import com.tom.mail.sender.dto.MailUpdateRequest;
import com.tom.mail.sender.dto.MailUserRequest;
import com.tom.mail.sender.dto.PageMailResponse;
import com.tom.mail.sender.model.Mail;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MailMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "content", expression = "java(request.content() != null ? request.content().getBytes(StandardCharsets.UTF_8) : null)")
	Mail build(MailRequest request);

	@Mapping(target = "id", ignore = true)
	Mail build(MailUserRequest request);
	
	@Mapping(target = "content", expression = "java(request.content() != null ? request.content().getBytes(StandardCharsets.UTF_8) : null)")
	MailResponse toResponse(Mail mail);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "content", expression = "java(request.content() != null ? request.content().getBytes(StandardCharsets.UTF_8) : null)")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Mail mail, MailUpdateRequest request);

	List<MailResponse> toResponseList(List<Mail> users);

	default PageMailResponse toResponse(Page<Mail> page) {
		if (page == null) {
			return null;
		}
		List<MailResponse> content = toResponseList(page.getContent());
		return new PageMailResponse(content, page.getNumber(), page.getSize(), page.getTotalPages(),
				page.getTotalElements());

	}

}
