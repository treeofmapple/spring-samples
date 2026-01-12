package com.tom.service.shortener.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Version;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Auditable {

	@Column(name = "date_created", updatable = false)
	private ZonedDateTime dateCreated;

	@Version
	@Column(name = "version", nullable = false)
	private Long version = 0L;

	@PrePersist
	protected void onCreate() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		this.dateCreated = now;
	}

}
