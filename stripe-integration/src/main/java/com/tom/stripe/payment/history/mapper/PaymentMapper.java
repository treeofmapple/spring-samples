package com.tom.stripe.payment.history.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.stripe.payment.history.dto.PagePaymentHistoryResponse;
import com.tom.stripe.payment.history.dto.PaymentHistoryResponse;
import com.tom.stripe.payment.history.model.PaymentHistory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

	@Mapping(target = "paymentId", source = "payment.id")
	PaymentHistoryResponse toResponse(PaymentHistory paymentHistory);

	List<PaymentHistoryResponse> toResponseList(List<PaymentHistory> payments);

	default PagePaymentHistoryResponse toResponse(Page<PaymentHistory> page) {
		if (page == null) {
			return null;
		}
		List<PaymentHistoryResponse> content = toResponseList(page.getContent());
		return new PagePaymentHistoryResponse(content, page.getNumber(), page.getSize(), page.getTotalPages());
	}

}
