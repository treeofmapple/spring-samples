package com.tom.benchmark.monolith.product;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	Product toProduct(ProductRequest request);

	ProductResponse toResponse(Product product);

}
