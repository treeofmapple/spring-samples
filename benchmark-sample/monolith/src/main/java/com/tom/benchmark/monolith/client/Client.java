package com.tom.benchmark.monolith.client;

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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client", indexes = {
		@Index(name = "idx_client_cpf", columnList = "cpf")
})
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", 
			nullable = false, 
			unique = false,
			updatable = true,
			length = 100)
	private String name;
	
    @Column(name = "cpf", 
    		nullable = false,
    		unique = true,
			updatable = true)
    private String cpf;
	
}
