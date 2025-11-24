package com.tom.awstest.lambda.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tom.awstest.lambda.dto.PageProductResponse;
import com.tom.awstest.lambda.dto.ProductRequest;
import com.tom.awstest.lambda.dto.ProductResponse;
import com.tom.awstest.lambda.dto.UpdateRequest;
import com.tom.awstest.lambda.model.Product;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	@Mapping(target = "id", ignore = true)
	Product build(ProductRequest request);
	
	ProductResponse toResponse(Product product);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void mergeData(@MappingTarget Product product, UpdateRequest request);
	
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
