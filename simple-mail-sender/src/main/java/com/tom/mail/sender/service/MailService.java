package com.tom.mail.sender.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.mail.sender.dto.MailRequest;
import com.tom.mail.sender.dto.MailResponse;
import com.tom.mail.sender.dto.MailUpdateRequest;
import com.tom.mail.sender.dto.MasterMailRequest;
import com.tom.mail.sender.dto.PageMailResponse;
import com.tom.mail.sender.logic.mail.MailSender;
import com.tom.mail.sender.mapper.MailMapper;
import com.tom.mail.sender.repository.MailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;
	
	private final MailRepository repository;
	private final MailMapper mailMapper;
	private final MailSender mailSender;
	
	@Transactional(readOnly = true)
	public MailResponse searchMailById(UUID userId) {
		return null;
	}
	
	@Transactional(readOnly = true)
	public PageMailResponse searchMailByParams(int page, String title) {
		return null;
	}
	
	@Transactional
	public MailResponse createMail(MailRequest request) {
		return null;
	}
	
	@Transactional
	public MailResponse sendMail(String title) {
		return null;
	}
	
	@Transactional
	public MailResponse sendMailToOnlyUsers() {
		return null;
	}
	
	@Transactional
	public MailResponse defineMasterMail(MasterMailRequest request) {
		return null;
	}
	
	@Transactional
	public MailResponse editMailInfo(MailUpdateRequest request) {
		return null;
	}
	
	@Transactional
	public void deleteMail(UUID mailId) {
		
	}
	
}
