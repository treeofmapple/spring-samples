package com.tom.mail.sender.global.constraints;

public final class MailConstraints {

	public static final int MAX_TITLE_LENGTH = 200;
    public static final int MAX_CONTENT_LENGTH = 50000;
    public static final int MAIL_BATCH_SIZE = 20;
    public static final int MAIL_WAIT_TIME = 40; // seconds
    public static final String TEMPLATE_FILE_EXTENSION = ".md";
	public static final String[] VARIABLES_FILE_EXTENSION = { ".csv", ".txt" };
    
    private MailConstraints() {}
	
}
