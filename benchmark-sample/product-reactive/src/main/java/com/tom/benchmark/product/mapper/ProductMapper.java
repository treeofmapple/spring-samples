package com.tom.benchmark.product.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.benchmark.product.dto.PageProductResponse;
import com.tom.benchmark.product.dto.ProductRequest;
import com.tom.benchmark.product.dto.ProductResponse;
import com.tom.benchmark.product.dto.ProductUpdate;
import com.tom.benchmark.product.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { UUID.class,
		java.time.ZonedDateTime.class })
public interface ProductMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
	Product build(ProductRequest request);

	ProductResponse toResponse(Product product);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Product product, ProductUpdate request);
	
	default PageProductResponse toResponse(List<Product> list, Integer page, Integer size) {
		List<ProductResponse> content = list.stream().map(this::toResponse).toList();
		return new PageProductResponse(content, page, size, 0, (long) list.size());
	}
}
