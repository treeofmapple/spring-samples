package com.tom.rabbitmq.realtime.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
