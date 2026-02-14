package com.tom.mail.sender.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tom.mail.sender.exception.sql.DataViolationException;
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

	public Set<String> addUsersToList(Mail mail, List<String> emails) {
		Set<String> usersOnDB = emails.stream()
	            .filter(e -> e != null && !e.isBlank())
	            .collect(Collectors.toSet());
		
		if (usersOnDB.isEmpty()) {
			throw new DataViolationException(String.format("The request contained no valid email addresses."));
		}

		mail.getUsers().addAll(usersOnDB);
		return usersOnDB;
	}

	public Set<String> removeUsersFromList(Mail mail, List<String> emailsToRemove) {
		var currentUsers = mail.getUsers();
		boolean changed = currentUsers.removeIf(emailsToRemove::contains);

		if (!changed) {
			throw new DataViolationException("None of the specified emails were associated with this mail.");
		}
		return currentUsers;
	}

}
