package com.tom.stripe.payment.payment.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;
import com.tom.stripe.payment.payment.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "payment")
public class Payment extends Auditable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "user_id", nullable = false)
    private UUID userId;
	
	@Column(name = "stripe_payment_intent_id", unique = true)
    private String stripePaymentIntentId;

	private String receiptNumber;

	@Column(length = 512)
	private String receiptUrl;
	
	@Column(precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;
	
	private String currency;
	
	private PaymentStatus paymentStatus;
	
}
