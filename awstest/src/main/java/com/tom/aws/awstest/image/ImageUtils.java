package com.tom.aws.awstest.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.tom.aws.awstest.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageUtils {

	private final ImageRepository repository;
	
    public Image findImageById(Long query) {
        return repository.findById(query).orElseThrow(
        		() -> new NotFoundException("Image not found: " + query));
    }
	
    public Image findImageByName(String query) {
    	return repository.findByName(query).orElseThrow(
    			() -> new NotFoundException("Image not found: " + query));
    }
    
    public Page<Image> findByIdOrNamePageable(String query, Pageable pageable) {
    	if(query == null || query.isBlank()) {
    		throw new NotFoundException("Image not found: " + query);
    	}
    	try {
    		Long userId = Long.parseLong(query);
    		return repository.findById(userId, pageable);
    	} catch(NotFoundException e) {
    		return repository.findByName(query, pageable);
		}
    }
    
    public Image findByIdOrName(String query) {
    	if(query == null || query.isBlank()) {
    		throw new NotFoundException("Image not found: " + query);
    	}
    	try {
    		Long userId = Long.parseLong(query);
    		return findImageById(userId);
    	} catch(RuntimeException e) {
    		return findImageByName(query);
		}
    }
    
    public void checkIfObjectAlreadyExists(Image currentImage, String newImageName) {
    	if(repository.existsByNameAndIdNot(newImageName, currentImage.getId())) {
    		throw new NotFoundException("Object already exists: " + newImageName);
    	}
    }
    
}
