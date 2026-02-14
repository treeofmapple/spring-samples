package com.tom.mail.sender.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.tom.mail.sender.global.constraints.MailConstraints;

public record MailUpdateRequest(
		
		@JsonAlias( {
			"mailId",
			"id" })
		UUID mailId,
		
		@Length(max = MailConstraints.MAX_TITLE_LENGTH)
		String title,
		
		@Length(max = MailConstraints.MAX_CONTENT_LENGTH)
		String content
		
) {

}
