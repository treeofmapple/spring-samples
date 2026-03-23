package com.tom.stripe.payment.logic.webhook;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stripe.net.Webhook;

@Repository
public interface WebhookLogsRepository extends JpaRepository<Webhook, UUID> {

}
