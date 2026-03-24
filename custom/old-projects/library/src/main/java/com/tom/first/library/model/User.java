package com.tom.first.library.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class User extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name= "username", nullable = false, unique = false, updatable = true)
	private String username;
	
	@Column(name= "email", nullable = false, unique = true, updatable = true)
	private String email;
	
	@Column(name= "password", nullable = false, unique = false, updatable = true)
	private String password;
	
	@Column(name= "age", nullable = false, unique = false, updatable = true)
	private int age;
	
	@OneToMany(mappedBy = "user")
	private List<BookItem> bookItems;
}
