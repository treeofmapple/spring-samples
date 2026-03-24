package com.tom.aws.awstest.models;


import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Version;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Auditable {

	@Column(name = "created_at", 
			nullable = false, 
			updatable = false)
	private ZonedDateTime createdAt;

	@Column(name = "updated_at", 
			nullable = false)
	private ZonedDateTime updatedAt;

	@Version
	@Column(name = "version", 
			nullable = false)
	private Long version = 0L;
	
	@PrePersist
	protected void onCreate() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = ZonedDateTime.now(ZoneOffset.UTC);
	}

}