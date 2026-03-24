package com.tom.aws.awstest.config;

import org.springframework.stereotype.Component;

import com.tom.aws.awstest.bucket.AwsFunctions;
import com.tom.aws.awstest.bucket.AwsProperties;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class SystemShutdown {

    private final AwsFunctions awsFunctions;
    private final AwsProperties r2Properties;

    @PreDestroy
    public void onShutdown() {
        String ddlAuto = r2Properties.getDdlAuto();
        
        if ("create-drop".equalsIgnoreCase(ddlAuto)) {
            log.info("R2 ddl-auto is set to 'create-drop'. Clearing R2 bucket: {}", r2Properties.getBucket());
            try {
                var keysToDelete = awsFunctions.listAllObjectKeys();
                
                for (String key : keysToDelete) {
                    awsFunctions.deleteObject(key);
                    log.info("Deleted object from R2: {}", key);
                }
                log.info("Successfully cleared R2 bucket.");
            } catch (Exception e) {
                log.error("Failed to clear R2 bucket on shutdown.", e);
            }
        } else {
            log.info("R2 ddl-auto is not 'create-drop'. Skipping bucket cleanup.");
        }
    }
	
}
