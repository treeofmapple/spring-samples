package com.tom.aws.awstest.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.aws.awstest.common.SecurityUtils;
import com.tom.aws.awstest.logic.GenData;
import com.tom.aws.awstest.product.dto.PageProductResponse;
import com.tom.aws.awstest.product.dto.ProductRequest;
import com.tom.aws.awstest.product.dto.ProductResponse;
import com.tom.aws.awstest.product.dto.UpdateRequest;

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
	private final GenData dataGeneration;
	
	@Transactional(readOnly = true)
	public PageProductResponse searchProductByParams(int page, String query) {
    	String userIp = securityUtils.getRequestingClientIp();
        log.info("User {} is finding product: {}", userIp, query);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        var product = productUtils.findByIdOrNamePageable(query, pageable);
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
    public void deleteProduct(long query) {
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

	public PageProductResponse generateProducts(int quantity) {
    	String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is generating {}: products", userIp, quantity);
		
		dataGeneration.processGenerateProduct(quantity);
		
		Pageable pageable = PageRequest.of(0, PAGE_SIZE);
		Page<Product> products = repository.findMostRecent(pageable);
		
		log.info("Products finished generation");
		return mapper.toResponse(products);
	}

    
}
