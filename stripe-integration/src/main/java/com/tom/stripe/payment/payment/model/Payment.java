package com.tom.stripe.payment.payment.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;
import com.tom.stripe.payment.history.model.PaymentHistory;
import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;

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
import jakarta.persistence.Transient;
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
@Table(name = "payment", indexes = { @Index(name = "index_payment_amount", columnList = "amount") })
public class Payment extends Auditable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ToString.Include
	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@ToString.Include
	@Column(name = "stripe_payment_intent_id", unique = true, nullable = false)
	private String stripePaymentIntentId;

	@Column(name = "receipt_number", unique = true, nullable = true)
	private String receiptNumber;

	@Transient
	private String receiptUrl;

	// indepotency key
	
	@ToString.Include
	@Column(precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;

	@ToString.Include
	@Column(name = "currency", nullable = false)
	private AcceptedCurrency currency;
	
	@ToString.Include
	@Column(name = "payment_method", nullable = false)
	private PaymentMethods paymentMethod;

	@OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PaymentHistory> paymentHistory;

}
