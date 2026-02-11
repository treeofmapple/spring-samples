package com.tom.mail.sender.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tom.mail.sender.exception.sql.NotFoundException;
import com.tom.mail.sender.model.Mail;
import com.tom.mail.sender.repository.MailRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailComponent {

	private final MailRepository repository;

	public Mail searchById(UUID mailId) {
		return repository.findById(mailId)
				.orElseThrow(() -> new NotFoundException(String.format("Mail with id: %s was not found", mailId)));
	}

}
