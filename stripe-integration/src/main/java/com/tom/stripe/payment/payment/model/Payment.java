package com.tom.stripe.payment.payment.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;
import com.tom.stripe.payment.history.model.PaymentHistory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
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
@Table(name = "payment", indexes = { @Index(name = "index_payment_amount", columnList = "amout") })
public class Payment extends Auditable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "stripe_payment_intent_id", unique = true, nullable = false)
	private String stripePaymentIntentId;

	@Column(name = "receiptNumber", unique = true, nullable = true)
	private String receiptNumber;

	@Column(name = "receiptUrl", nullable = true)
	private String receiptUrl;

	@Column(precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;

	@Column(name = "currency", nullable = false)
	private String currency;

	@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PaymentHistory paymentHistory;

}
