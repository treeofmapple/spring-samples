package com.tom.first.datajpa.model.embedded;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrderId implements Serializable {

	private String username;
	
	private LocalDateTime orderDate;
	
}
