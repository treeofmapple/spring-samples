package com.tom.aws.awstest.bucket;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Primary
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProperties {
 
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private String region;
    private String ddlAuto;
    private boolean accelerateEnabled;
}
