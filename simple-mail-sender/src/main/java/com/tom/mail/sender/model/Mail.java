package com.tom.mail.sender.model;

import java.util.List;
import java.util.UUID;

import com.tom.mail.sender.global.Auditable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "mail", indexes = { @Index(name = "index_mail_title", columnList = "title"), })
public class Mail extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "title", length = 80, unique = true, updatable = true, nullable = false)
	private String title;
	
	@Column(name = "mails", updatable = true, nullable = false)
	private List<String> mails;
	
	// store the bytes of the mail to send can be (.html , .md or .txt)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "content", nullable = true, unique = false)
	private byte[] content;
	
}
