package com.tom.stripe.payment.user.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;
import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;

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
@Table(name = "users", indexes = { @Index(name = "index_user_email", columnList = "email"),
		@Index(name = "index_user_balance", columnList = "balance") })
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

	@Column(name = "tax_id", length = 20, nullable = true)
	private String taxId; // cpf

	@Column(name = "postal_code", length = 20, nullable = true)
	private String postalCode;

	@Column(name = "country_code", length = 2, nullable = true)
	private String countryCode;

	@Column(name = "balance", precision = 15, scale = 2, nullable = false)
	private BigDecimal balance;

	@ToString.Include
	@Column(name = "stripe_customer_id", nullable = true)
	private String stripeCustomerId;

	@ToString.Include
	@Column(name = "preffered_currency", nullable = true)
	private AcceptedCurrency currencyPreferred;

	@ToString.Include
	@Column(name = "preffered_paymentMethod", nullable = true)
	private PaymentMethods defaultPaymentMethods;

}
