package com.tom.stripe.payment.logic.webhook;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WebhookLogsMapper {

	WebhookLogs build(WebhookLogsRequest request);

}
