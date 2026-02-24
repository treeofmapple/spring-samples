package com.tom.stripe.payment.logic.webhook;

import java.util.UUID;

import com.tom.stripe.payment.global.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "webhook_logs", indexes = { @Index(name = "idx_stripe_event_id", columnList = "stripe_event_id") })
public class WebhookLogs extends Auditable {

	@Id
	@ToString.Include
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "stripe_event_id", unique = true, nullable = false)
	private String stripeEventId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@ToString.Include
	@Enumerated(EnumType.STRING)
	@Column(name = "webhook_status", nullable = false)
	private WebhookStatus status;

	@ToString.Include
	@Column(name = "logs", columnDefinition = "TEXT", nullable = true)
	private String logs;

	@Column(name = "error_message", columnDefinition = "TEXT", nullable = true)
	private String errorMessage;

}
