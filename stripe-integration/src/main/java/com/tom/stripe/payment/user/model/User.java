package com.tom.stripe.payment.user.model;

import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;

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
import lombok.ToString;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "users", indexes = { @Index(name = "index_user_email", columnList = "email") })
public class User extends Auditable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ToString.Include
	@Column(name = "nickname", unique = true, nullable = false)
	private String nickname;
	
	@ToString.Include
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "stripe_customer_id", nullable = true)
    private String stripeCustomerId;
	
	@Column(name = "default_payment_method_id", nullable = true)
    private String defaultPaymentMethodId;
	
}
