package com.tom.mail.sender.logic.mail;

import org.springframework.stereotype.Component;

@Component
public class EmailSessionConfig {
	private String masterEmail;
	private Boolean masterMailDefined = false;

	public void setMasterEmail(String email) {
		this.masterEmail = email;
		this.masterMailDefined = (email != null && !email.isBlank());
	}

	public String getMasterEmail() {
		return masterEmail;
	}

	public Boolean isMasterMailDefined() {
		return masterMailDefined;
	}
}