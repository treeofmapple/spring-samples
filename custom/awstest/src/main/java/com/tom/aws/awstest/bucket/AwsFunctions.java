package com.tom.aws.awstest.bucket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tom.aws.awstest.image.Image;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

@Log4j2
@Component
@RequiredArgsConstructor
public class AwsFunctions {

	private final AwsProperties awsProperties;
	private final S3Client s3Client;
	
	public String buildS3Url(String key) {
	    String s3Url = s3Client
	    		.utilities()
	    		.getUrl(builder -> builder.bucket(awsProperties.getBucket()).key(key))
	    		.toExternalForm();
	    return s3Url;
	}
	
	public String uploadObject(String key, MultipartFile file) {
		log.info("Uploading object with key '{}' to bucket '{}'", key, 
				awsProperties.getBucket());
		try {
		    s3Client
	    		.putObject(PutObjectRequest.builder()
		        .bucket(awsProperties.getBucket())
		        .key(key)
		        .contentType(file.getContentType())
		        .build(),
		        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
	        );
		    String s3Url = buildS3Url(key);
		    return s3Url;
		} catch (IOException e) {
			log.error("Error uploading image", e);
			throw new RuntimeException("Error uploading image", e);
		}
	}
	
	public byte[] downloadObject(String key) {
		return s3Client
				.getObjectAsBytes(
						GetObjectRequest.builder()
						.bucket(awsProperties.getBucket())
						.key(key)
						.build())
				.asByteArray();
	}
	
	public void deleteObject(String key) {
		try {
			s3Client
				.deleteObject(
				DeleteObjectRequest.builder()
				.bucket(awsProperties.getBucket())
				.key(key)
				.build());
			log.info("Successfully deleted object with key '{}'", key);
		} catch (S3Exception e) {
			log.error("Error deleting object with key '{}'", key, e);
			throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
		}
	}
	
	public String renameObject(Image image, String newName) {
	    String originalKey = image.getObjectKey();
	    
	    String extension = "";
	    if (originalKey.contains(".")) {
	        extension = originalKey.substring(originalKey.lastIndexOf("."));
	    }
	    
		String sanitizedNewName = 
				newName.contains(".") ? 
						newName.substring(0, newName.lastIndexOf(".")) : newName;
	    
		String newKey = sanitizedNewName + extension;
	    
	    log.info("Renaming object from key '{}' to '{}'", originalKey, newKey);

	    try {
	        s3Client.copyObject(CopyObjectRequest.builder()
	                .sourceBucket(awsProperties.getBucket())
	                .sourceKey(originalKey)
	                .destinationBucket(awsProperties.getBucket())
	                .destinationKey(newKey)
					.build()
	        );
	        log.info("Successfully copied object to new key '{}'", newKey);

	        deleteObject(image.getObjectKey());

	        return newKey;

	    } catch (S3Exception e) {
	        log.error("Failed to rename object with key '{}'. Error: {}", originalKey, e.getMessage());
	        try {
	            log.warn("Attempting to clean up by deleting partially renamed object with key '{}'", newKey);
	            s3Client.deleteObject(b -> b.bucket(
	            		awsProperties.getBucket()).key(newKey));
	        } catch (S3Exception cleanupException) {
	            log.error("Failed to clean up (delete) the new object with key '{}'. Manual cleanup may be required.", newKey, cleanupException);
	        }
	        throw new RuntimeException("Could not rename the file: " + e.getMessage(), e);
	    }
	}
	
	public List<String> listAllObjectKeys() {
	    List<String> keys = new ArrayList<>();
	    try {
	        ListObjectsV2Request request = ListObjectsV2Request.builder()
	                .bucket(awsProperties.getBucket())
	                .build();
	        
	        ListObjectsV2Iterable responses = s3Client.listObjectsV2Paginator(request);

	        for (ListObjectsV2Response page : responses) {
	            page.contents().forEach(object -> keys.add(object.key()));
	        }
	        log.info("Found {} objects in bucket '{}' for cleanup.", keys.size(), awsProperties.getBucket());
	    } catch (S3Exception e) {
	        log.error("Error listing objects in S3 bucket for cleanup.", e);
	    }
	    return keys;
	}
	
	/*
	
	public void addTags(Image image, String tagKey, String tagValue) {
		awsConfig.getS3Client()
			.putObjectTagging(PutObjectTaggingRequest.builder()
		    .bucket(awsConfig.getBucketName())
		    .key(image.getObjectKey())
		    .tagging(Tagging.builder()
		        .tagSet(List.of(Tag.builder()
		        		.key(tagKey)
		        		.value(tagValue)
		        		.build())).build())
		    .build());
	}
	
	public void removeTag(Image image, String tagKey, String tagValue) { // remove one tag from a current image
	    awsConfig.getS3Client().putObjectTagging(
	            PutObjectTaggingRequest.builder()
	                .bucket(awsConfig.getBucketName())
	                .key(image.getObjectKey())
	                .tagging(Tagging.builder()
	                    .tagSet(List.of(Tag.builder()
	                    		.key(tag)
	                    		))
	                    .build())
	                .build()
        );
	}
	
	public void deleteTags(String tagKey, String tagValue) { // delete the tag, and if is on a image remove it also
		
	}
	
	*/
	
}
