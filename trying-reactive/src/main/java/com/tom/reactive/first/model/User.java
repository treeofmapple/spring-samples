package com.tom.reactive.first.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
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
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User {

	@Id
	private UUID id;

	private String nickname;
	private String email;
	private String password;

	@Column("phone_number")
	private String phoneNumber;

	private String role;
	private String bio;

	private Boolean active;

	@CreatedDate
	@Column("created_at")
	private LocalDateTime createdAt;

}
