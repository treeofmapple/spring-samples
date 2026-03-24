package com.tom.aws.awstest.config;

import org.springframework.stereotype.Component;

import com.tom.aws.awstest.bucket.AwsProperties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Log4j2
@Component
@RequiredArgsConstructor
public class SystemStart {

    private final AwsProperties properties;
	private final S3Client s3Client;
	
	@Getter
	private boolean s3Connected = false;
	
	@PostConstruct
	public void verifyS3Connection() {
	    try {
	        s3Client.getBucketLocation(b -> b.bucket(properties.getBucket()));
	        log.info("Bucket '{}' exists. Connection successful.", properties.getBucket());
	        s3Connected = true;
	    } catch (S3Exception e) {
	        if (e.statusCode() == 404) {
	            log.warn("Bucket '{}' does not exist. Creating it...", properties.getBucket());
	            try {
	                s3Client.createBucket(b -> b.bucket(properties.getBucket()));
	                log.info("Bucket '{}' created successfully.", properties.getBucket());
	                s3Connected = true;
	            } catch (S3Exception ex) {
	                log.error("Failed to create bucket '{}': {}", properties.getBucket(), ex.awsErrorDetails().errorMessage());
	                s3Connected = false;
	            }
	        } else {
	            log.error("S3 connection failed: {}", e.awsErrorDetails().errorMessage(), e);
	            s3Connected = false;
	        }
	    } catch (Exception e) {
	        log.error("Unexpected error while connecting to S3: {}", e.getMessage(), e);
	        s3Connected = false;
	    }
	}
	
}