package com.tom.front.full.model;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee", indexes = {
	@Index(name = "idx_employee_employee_code", columnList = "employee_code"),
})
public class Employee {

	@Value("${application.employee.code.length:8}")
	public static final int EMPLOYEE_CODE_LENGTH = 8;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name",
			length = 255,
			nullable = false,
			unique = false,
			updatable = true)
	private String name;
	
	@Column(name = "email",
			length = 255,
			nullable = false,
			unique = true,
			updatable = true)
	private String email;
	
	@Column(name = "job_title",
			length = 100,
			nullable = false,
			unique = false,
			updatable = true)
	private String jobTitle;
	
	@Column(name = "phone",
			length = 40,
			nullable = true,
			unique = false,
			updatable = true)
	private String phone;
	
	@Column(name = "image_url",
			length = 1024,
			nullable = true,
			unique = false,
			updatable = true)
	private String imageUrl;

	@Column(name = "employee_code",
			length = EMPLOYEE_CODE_LENGTH,
			nullable = false,
			unique = true,
			updatable = true)
	private String employeeCode;
	
}
