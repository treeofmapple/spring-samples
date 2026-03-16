package com.tom.security.hash.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableCaching
@Profile("cluster")
public class HazelConfig {

	@Value("${cache.cluster-name}")
	private String hazelcastName;
	
	@Value("${cache.address:}")
	private String hazelAddress;
	
	@Value("${cache.name.login-attempt}")
	private String loginCacheName;

	@Value("${cache.time.login-attempt:15m}")
	private Duration loginCacheTime;

	@Bean
	HazelcastInstance hazelcastInstance() {
		if (hazelAddress == null || hazelAddress.isBlank()) {
            log.warn("No cache address provided. Starting in Embedded mode directly.");
            return createEmbeddedInstance();
        }
		
		try {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setClusterName(hazelcastName);
			clientConfig.getNetworkConfig().addAddress(hazelAddress);
			clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig().setClusterConnectTimeoutMillis(2000);
			
			log.info("Attempt to connect on external Hazelcast at: {}", hazelAddress);
			return HazelcastClient.newHazelcastClient(clientConfig);
		} catch (Exception e) {
			log.warn("Could not connect to external Hazelcast cluster. Using Embedded mode.");
			return createEmbeddedInstance();
		}
	}
	
	private HazelcastInstance createEmbeddedInstance() {
        Config internalConfig = new Config();
        internalConfig.setClusterName(hazelcastName);
        internalConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        
        MapConfig loginConfig = new MapConfig();
        loginConfig.setName(loginCacheName);
        loginConfig.setTimeToLiveSeconds((int) loginCacheTime.toSeconds());
        internalConfig.addMapConfig(loginConfig);
        
        return Hazelcast.newHazelcastInstance(internalConfig);
    }
	
}
