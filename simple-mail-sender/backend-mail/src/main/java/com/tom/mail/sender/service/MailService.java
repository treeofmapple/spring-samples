package com.tom.mail.sender.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tom.mail.sender.dto.MailRequest;
import com.tom.mail.sender.dto.MailResponse;
import com.tom.mail.sender.dto.MailUpdateRequest;
import com.tom.mail.sender.dto.MailUserRequest;
import com.tom.mail.sender.dto.MasterMailRequest;
import com.tom.mail.sender.dto.PageMailResponse;
import com.tom.mail.sender.exception.sql.DataViolationException;
import com.tom.mail.sender.exception.sql.NotFoundException;
import com.tom.mail.sender.exception.system.VariablesNotDefinedException;
import com.tom.mail.sender.logic.mail.EmailSessionConfig;
import com.tom.mail.sender.logic.mail.MailContentProcessor;
import com.tom.mail.sender.logic.mail.MailSender;
import com.tom.mail.sender.logic.mail.MailUtils;
import com.tom.mail.sender.mapper.MailMapper;
import com.tom.mail.sender.model.Mail;
import com.tom.mail.sender.repository.MailRepository;
import com.tom.mail.sender.repository.MailSpecification;

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
	private final MailUtils mailUtils;
	private final MailComponent mailComponent;
	private final MailContentProcessor mailProcessor;
	private final EmailSessionConfig emailConfig;

	@Transactional(readOnly = true)
	public MailResponse searchMailById(UUID mailId) {
		var mail = mailComponent.searchById(mailId);
		return mailMapper.toResponse(mail);
	}

	@Transactional(readOnly = true)
	public PageMailResponse searchMailByParams(int page, String title, List<String> users) {
		Specification<Mail> spec = MailSpecification.findByCriteria(title, users);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Mail> mail = repository.findAll(spec, pageable);
		return mailMapper.toResponse(mail);
	}

	@Transactional
	public MailResponse createMail(MailRequest request) {
		var mail = mailMapper.build(request);
		if (repository.existsByTitle(request.title())) {
			throw new DataViolationException(String.format("Mail with title: %s, already exists.", request.title()));
		}

		var savedMail = repository.save(mail);
		return mailMapper.toResponse(savedMail);
	}

	@Transactional
	public MailResponse addCustomContent(UUID mailId, MultipartFile template, MultipartFile variablesFile) {
		var mail = mailComponent.searchById(mailId);
		mail.setContent(mailProcessor.processEmailTemplate(template, variablesFile));
		return mailMapper.toResponse(mail);
	}

	@Transactional
	public MailResponse sendMail(UUID mailId) {
		if (!emailConfig.getMasterMailDefined()) {
			throw new VariablesNotDefinedException(String.format("Define mail sender variables before sending mail."));
		}
		var mail = mailComponent.searchById(mailId);
		if (mail.getUsers() == null || mail.getUsers().isEmpty()) {
			throw new VariablesNotDefinedException(String.format("Email didn't define users to sent mail"));
		}
		mailSender.sendEmailWithAttachment(mail);
		mail.getSentOnTime().add(LocalDateTime.now());
		return mailMapper.toResponse(mail);
	}

	@Transactional
	public MailResponse addUsersMails(MailUserRequest request) {
		var mail = mailComponent.searchById(request.mailId());
		mailComponent.addUsersToList(mail, request.mails());
		var updatedMail = repository.save(mail);
		return mailMapper.toResponse(updatedMail);
	}

	@Transactional
	public MailResponse removeUsersMails(MailUserRequest request) {
		var mail = mailComponent.searchById(request.mailId());
		mailComponent.removeUsersFromList(mail, request.mails());
		var updatedMail = repository.save(mail);
		return mailMapper.toResponse(updatedMail);
	}

	@Transactional
	public Boolean defineMasterMail(MasterMailRequest request) {
		mailMapper.update(emailConfig, request);
		mailUtils.testConnection();
		return emailConfig.isMasterMailDefined();
	}

	@Transactional(readOnly = true)
	public String whatIsTheMasterMail() {
		return Objects.requireNonNullElse(emailConfig.getMasterEmail(), "No master email defined");
	}

	@Transactional
	public MailResponse editMailInfo(MailUpdateRequest request) {
		var mail = mailComponent.searchById(request.mailId());
		mailMapper.update(mail, request);
		var updatedMail = repository.save(mail);
		return mailMapper.toResponse(updatedMail);
	}

	@Transactional
	public void deleteMail(UUID mailId) {
		if (repository.existsById(mailId)) {
			throw new NotFoundException(String.format("Mail with %s, was not found.", mailId));
		}
		repository.deleteById(mailId);
	}

}
