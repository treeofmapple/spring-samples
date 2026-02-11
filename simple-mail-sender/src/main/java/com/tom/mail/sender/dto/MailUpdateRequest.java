package com.tom.mail.sender.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.tom.mail.sender.global.constraints.MailConstraints;

public record MailUpdateRequest(
		
		UUID id,
		
		@Length(max = MailConstraints.MAX_TITLE_LENGTH)
		String title,
		
		@Length(max = MailConstraints.MAX_CONTENT_LENGTH)
		String content
		
) {

}
