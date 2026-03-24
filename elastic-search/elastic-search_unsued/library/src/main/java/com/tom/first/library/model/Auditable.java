package com.tom.first.library.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Auditable {

	@Column(updatable = false)
	private ZonedDateTime createdDate;

	private ZonedDateTime lastModifiedDate;

	@PrePersist
	private void onCreate() {
		ZonedDateTime now = ZonedDateTime.now();
		this.createdDate = now;
		this.lastModifiedDate = now;
	}

	@PreUpdate
	private void onUpdate() {
		this.lastModifiedDate = ZonedDateTime.now();
	}

}
