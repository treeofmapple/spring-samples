package com.tom.benchmark.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.tom.benchmark.product.dto.ProductRequest;
import com.tom.benchmark.product.dto.ProductResponse;
import com.tom.benchmark.product.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	Product toProduct(ProductRequest request);
	
	ProductResponse toResponse(Product product);
	
	// @Mapping(target = "id", ignore = true)
	// void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
	
}
