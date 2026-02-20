package com.tom.stripe.payment.payment.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.stripe.payment.payment.dto.PagePaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentRequest;
import com.tom.stripe.payment.payment.dto.PaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentUpdate;
import com.tom.stripe.payment.payment.model.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

	Payment build(PaymentRequest request);

	PaymentResponse toResponse(Payment payment);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Payment payment, PaymentUpdate request);

	List<PaymentResponse> toResponseList(List<Payment> payments);

	default PagePaymentResponse toResponse(Page<Payment> page) {
		if (page == null) {
			return null;
		}
		List<PaymentResponse> content = toResponseList(page.getContent());
		return new PagePaymentResponse(content, page.getNumber(), page.getSize(), page.getTotalPages());
	}

}
