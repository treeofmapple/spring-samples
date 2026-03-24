package com.tom.aws.awstest.product;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tom.aws.awstest.product.dto.PageProductResponse;
import com.tom.aws.awstest.product.dto.ProductRequest;
import com.tom.aws.awstest.product.dto.ProductResponse;
import com.tom.aws.awstest.product.dto.UpdateRequest;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	@Mapping(target = "id", ignore = true)
	Product build(ProductRequest request);
	
	ProductResponse toResponse(Product product);
	
	void mergeData(@MappingTarget Product product, ProductRequest request);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void mergeData(@MappingTarget Product product, UpdateRequest request);
	
	Product mergeData(@MappingTarget Product product, String name, int quantity, BigDecimal price,
			String manufacturer, boolean active);
	
    List<ProductResponse> toResponseList(List<Product> product);
    
    default PageProductResponse toResponse(Page<Product> page) {
    	if(page == null) {
    		return null;
    	}
		List<ProductResponse> content = toResponseList(page.getContent());
		return new PageProductResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
    }

}
