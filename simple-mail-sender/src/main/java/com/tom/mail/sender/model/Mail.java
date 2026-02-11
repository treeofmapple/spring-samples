package com.tom.mail.sender.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.tom.mail.sender.global.Auditable;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "content", nullable = true, unique = false)
	private byte[] content;
	
	@ElementCollection
	@CollectionTable(name = "mail_users", joinColumns = @JoinColumn(name = "mail_id"))
	@Column(name = "users_email", nullable = true)	
	private List<String> users;
	
	@Column(name = "time_sent", nullable = true)
	private LocalDateTime sentOnTime;
	
}
