package com.tom.mail.sender.logic.mail;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class MailSender {

	private final JavaMailSender mailSender;
	private final EmailSessionConfig mailConfig;

	public void sendEmailWithAttachment() {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(mailConfig.getMasterEmail());
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setText(body);
			mimeMessageHelper.setSubject(subject);
			FileSystemResource fileSystem = new FileSystemResource(new File(attachment));
			mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			log.error("Message wasn't able to be sent", e);
		}
	}

}
