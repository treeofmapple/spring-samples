package com.tom.mail.sender.global.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.global.constraints.MailConstraints;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfigInfo {

    @Value("${application.size.Page:20}")
    private long pageSize;

    public SystemInfo fetchSystemInformations() {
        return new SystemInfo(
        		pageSize,
        		MailConstraints.MAX_TITLE_LENGTH,
        		MailConstraints.MAX_CONTENT_LENGTH,
        		MailConstraints.MAIL_BATCH_SIZE,
        		MailConstraints.MAIL_WAIT_TIME,
        		MailConstraints.TEMPLATE_FILE_EXTENSION,
        		String.join(", ", MailConstraints.VARIABLES_FILE_EXTENSION)
        );
    }
}
