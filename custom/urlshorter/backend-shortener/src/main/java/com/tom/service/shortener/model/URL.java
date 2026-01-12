package com.tom.service.shortener.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "urls", indexes = {
		@Index(name = "idx_short_shortUrl", columnList = "shortUrl", unique = true)
})
public class URL extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "UUID", // Change this line if you wanna use mysqlDB
			updatable = false, 
			nullable = false)
	private UUID id;
	
	@Column(name = "original_url", 
			nullable = false)
	private String originalUrl;
	
	@Column(name = "short_url", 
			nullable = false, 
			unique = true)
	private String shortUrl;
	
	@Column(name = "access_count", 
			nullable = false,
			columnDefinition = "int default 0")
	private Integer accessCount = 0;
	
	@Column(name = "expiration_time")
	private ZonedDateTime expirationTime;
	
	@Column(name = "last_access_time")
	private ZonedDateTime lastAccessTime;
}
