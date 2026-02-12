package com.tom.mail.sender.dto;

import java.util.List;

import jakarta.validation.constraints.Email;

public record MailUserRequest(

		List<@Email(message = "Only valid mails") String> mails

) {

}
