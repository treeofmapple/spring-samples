package com.tom.benchmark.client.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client {

	@Id
	@ToString.Include
	private UUID id;
	
	@ToString.Include
	@Column("name") //, nullable = false, unique = false, updatable = true, length = 100)
	private String name;
	
	@ToString.Include
	@Column("cpf") //, nullable = false, unique = true, updatable = true)
	private String cpf;
	
	@ReadOnlyProperty
	@Column("created_at")
	private ZonedDateTime createdAt; // = ZonedDateTime.now(ZoneOffset.UTC);
	
}
