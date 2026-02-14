package com.tom.mail.sender.logic.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class EmailSessionConfig {

	@Value("${mail.mail-server:localhost}")
	private String mailServer;

	private String masterEmail = "test@example.com";
	private String password = "password";
	private Boolean masterMailDefined = true;

	public void setMasterEmail(String email) {
		this.masterEmail = email;
		this.masterMailDefined = (email != null && !email.isBlank());
	}

	public Boolean isMasterMailDefined() {
		return masterMailDefined;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public Integer getPort() {
		if ("localhost".equalsIgnoreCase(this.mailServer) || "mail".equalsIgnoreCase(this.mailServer)
				|| "mail-server".equalsIgnoreCase(this.mailServer)) {
			return 1025;
		}
		return 587;
	}

}