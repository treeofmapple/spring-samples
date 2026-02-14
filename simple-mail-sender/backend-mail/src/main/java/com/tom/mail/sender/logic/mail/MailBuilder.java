package com.tom.mail.sender.logic.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.model.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class MailBuilder {

	private final JavaMailSender mailSender;
	private final EmailSessionConfig mailConfig;

	public void buildMail(Mail mail, String rawTemplate, String mailTo) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

			mimeHelper.setTo(mailTo);

			mimeHelper.setFrom(mailConfig.getMasterEmail());
			mimeHelper.setSubject(mail.getTitle());

			String personalizedContent = rawTemplate.replace("${mail}", mailTo).replace("{{mail}}", mailTo);
			mimeHelper.setText(personalizedContent, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Message wasn't able to be sent", e);
		}
	}

}
