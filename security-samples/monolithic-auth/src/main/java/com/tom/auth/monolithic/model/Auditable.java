package com.tom.auth.monolithic.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Auditable {

	@Column(name = "created_at", nullable = false, updatable = false)
	private ZonedDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private ZonedDateTime updatedAt;

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private UUID createdBy;

	@LastModifiedBy
	@Column(name = "modified_by")
	private UUID lastModifiedBy;
	
    @Version
    @Column(name = "version", nullable = false)
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
