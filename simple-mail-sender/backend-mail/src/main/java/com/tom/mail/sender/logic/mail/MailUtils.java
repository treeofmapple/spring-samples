package com.tom.mail.sender.logic.mail;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.exception.system.InternalException;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class MailUtils {

	private final JavaMailSender mailSender;
	private final EmailSessionConfig mailConfig;

	public void testConnection() {
		if (mailSender instanceof JavaMailSenderImpl impl) {
			impl.setHost(mailConfig.getMailServer());
			impl.setPort(mailConfig.getPort());

			Properties props = new Properties();
			boolean isLocal = "localhost".equalsIgnoreCase(mailConfig.getMailServer());

			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", mailConfig.getMailServer());
			props.put("mail.smtp.port", String.valueOf(mailConfig.getPort()));

			if (isLocal) {
				props.put("mail.smtp.auth", "false");
				props.put("mail.smtp.starttls.enable", "false");
			} else {
				impl.setUsername(mailConfig.getMasterEmail());
				impl.setPassword(mailConfig.getPassword());
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
			}

			impl.setJavaMailProperties(props);

			try {
				var transport = impl.getSession().getTransport("smtp");
				if (isLocal) {
					transport.connect(impl.getHost(), impl.getPort(), null, null);
				} else {
					transport.connect(impl.getHost(), impl.getPort(), impl.getUsername(), impl.getPassword());
				}
				transport.close();
				log.info("SMTP Connection successful!");
			} catch (MessagingException e) {
				log.error("SMTP Connection failed!");
				throw new InternalException("Email service is unreachable");
			}
		}
	}

}
