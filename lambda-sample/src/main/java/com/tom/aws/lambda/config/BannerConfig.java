package com.tom.aws.lambda.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;

import com.tom.aws.lambda.common.CustomBanner;

public class BannerConfig implements SpringApplicationRunListener {
    
    public BannerConfig(SpringApplication application, String[] args) {
        application.setBanner(new CustomBanner());
    }
}