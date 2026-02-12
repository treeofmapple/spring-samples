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

	public Set<String> addUsersToList(Mail mail, List<String> emails){
		var users = repository.findByUsersIn(emails);
		
		Set<String> newList = users.stream().filter(d -> repository.existsByUsersContaining(users)).collect(Collectors.toSet());
		
		if(newList.isEmpty()) {
			throw new DataViolationException(String.format(""));
		}
		
		mail.getUsers().clear();
		return newList;
	}
	
	public Set<String> removeUsersToList(Mail mail, List<String> emails){
		var users = repository.findByUsersIn(emails);
		
		Set<String> newList = users.stream().filter(d -> repository.existsByUsersContaining(users)).collect(Collectors.toSet());
		
		if(newList.isEmpty()) {
			throw new DataViolationException(String.format(""));
		}
		
		mail.getUsers().clear();
		return newList;
	}
	
}
