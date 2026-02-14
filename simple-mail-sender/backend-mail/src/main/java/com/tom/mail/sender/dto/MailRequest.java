package com.tom.mail.sender.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tom.mail.sender.global.constraints.MailConstraints;

import jakarta.validation.constraints.NotBlank;

public record MailRequest(
		
		@NotBlank(message = "Mail title cannot be blank.")
		@Length(max = MailConstraints.MAX_TITLE_LENGTH)
		String title,
		
		@JsonProperty("content")
		@Length(max = MailConstraints.MAX_CONTENT_LENGTH)
		String content
		
) {
	
}
