package com.tom.mail.sender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record MasterMailRequest(
		
		@JsonAlias( {
				"mail",
				"email" })
		@NotEmpty(message = "Master mail cannot be empty")
		@Email(message = "Mail needs to be validated.")
		String mail,
		
		@NotEmpty(message = "Password Cannot be empty")
		String password,

		String mailServer

	){

}
