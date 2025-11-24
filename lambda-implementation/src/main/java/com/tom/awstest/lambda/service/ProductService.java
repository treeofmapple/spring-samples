package com.tom.awstest.lambda.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.awstest.lambda.common.SecurityUtils;
import com.tom.awstest.lambda.dto.PageProductResponse;
import com.tom.awstest.lambda.dto.ProductRequest;
import com.tom.awstest.lambda.dto.ProductResponse;
import com.tom.awstest.lambda.dto.UpdateRequest;
import com.tom.awstest.lambda.mapper.ProductMapper;
import com.tom.awstest.lambda.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;
	
	private final ProductRepository repository;
	private final ProductMapper mapper;
	private final ProductUtil productUtils;
	private final SecurityUtils securityUtils;
	
	@Transactional(readOnly = true)
	public PageProductResponse searchProductName(int page, String query) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is finding product: {}", userIp, query);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        var product = productUtils.findByNamePageable(query, pageable);
        return mapper.toResponse(product);
    }

	@Transactional(readOnly = true)
	public ProductResponse searchProductById(long query) {
		var product = productUtils.findById(query);
		return mapper.toResponse(product);
	}
    
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is creating a new product: {}", userIp, request.name());
        try {
        	productUtils.ensureUniqueName(request.name());

        	var newProduct = mapper.build(request);
        	newProduct.setActive(true);
        	var product = repository.save(newProduct);
        	
        	log.info("Product created successfully with id: {}", product.getId());
        	return mapper.toResponse(product);
        } catch (DataIntegrityViolationException e) {
        	throw new RuntimeException(e);
        }
        
    }

    @Transactional
    public ProductResponse updateProduct(long query, UpdateRequest request) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is updating product: {}", userIp, query);

        var productToUpdate = productUtils.findById(query);
        mapper.mergeData(productToUpdate, request);
        var saved = repository.save(productToUpdate); 
        
        log.info("Product updated successfully with id: {}", query);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void deleteProductById(long query) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is deleting product: {}", userIp, query);
        
        var product = productUtils.findById(query);
        
        repository.deleteById(product.getId());
        log.info("Product deleted successfully: {}", query);
    }

    @Transactional
    public void setProduteState(long query) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is activating product: {}", userIp, query);
        
        var product = productUtils.findById(query);
        boolean currentState = product.getActive();
        product.setActive(!currentState);
        
        repository.save(product);
        log.info("Product status toggled. New state for product {}: {}", product.getId(), !currentState);
    }

}
