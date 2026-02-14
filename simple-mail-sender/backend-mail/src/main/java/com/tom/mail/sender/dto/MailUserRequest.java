package com.tom.mail.sender.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailUserRequest(

		@JsonAlias( {
				"mailId",
				"id" })
		UUID mailId,
		
		@Valid
		List<@NotBlank(message = "Mails can't be null") @Email(message = "Only valid mails") String> mails

) {

}
