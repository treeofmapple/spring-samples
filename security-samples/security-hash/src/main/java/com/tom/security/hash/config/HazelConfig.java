package com.tom.security.hash.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class HazelConfig {

	@Value("${cache.cluster-name}")
	private String hazelCastName;
	
	@Value("${cache.address}")
	private String hazelAddresss;
	
	@Value("${cache.name.login-attempt}")
	private String loginCacheName;

	@Value("${cache.time.login-attempt:15m}")
	private Duration loginCacheTime;

	@Bean
	HazelcastInstance hazelcastInstance() {
		Config embeddedConfig = new Config();
		embeddedConfig.setClusterName(hazelCastName);
		
		MapConfig mapConfig = new MapConfig(loginCacheName).setTimeToLiveSeconds((int) loginCacheTime.toSeconds());
		embeddedConfig.addMapConfig(mapConfig);

		embeddedConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

		try {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setClusterName(hazelCastName);
			clientConfig.getNetworkConfig().addAddress(hazelAddresss);
			clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig().setClusterConnectTimeoutMillis(2000);
			return HazelcastClient.newHazelcastClient(clientConfig);
		} catch (Exception e) {
			log.warn("Could not connect to external Hazelcast cluster. Falling back to Embedded mode.");
			return Hazelcast.newHazelcastInstance(embeddedConfig);
		}
	}

}
