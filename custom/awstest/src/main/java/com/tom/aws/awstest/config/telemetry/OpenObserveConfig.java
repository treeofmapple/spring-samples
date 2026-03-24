package com.tom.aws.awstest.config.telemetry;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import net.logstash.logback.encoder.LogstashEncoder;

@Configuration
@ConditionalOnProperty(name = "openobserve.enabled", havingValue = "true", matchIfMissing = true)
public class OpenObserveConfig {

    @Value("${openobserve.url}")
    private String openObserveUrl;

    @Value("${openobserve.username:root@example.com}")
    private String username;

    @Value("${openobserve.password:Complexpass#123}")
    private String password;
	
    @PostConstruct
    public void setup() {
        try {
            System.out.println("Testing OpenObserve Connection");

            if (!isConnectionWorking(openObserveUrl, username, password)) {
                System.err.println("OpenObserve Connection Failed");
                return; 
            }
            System.out.println("Connection Successful! Configuring Appender: " + openObserveUrl);
        	
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            LogstashEncoder encoder = new LogstashEncoder();
            encoder.setContext(context);
            encoder.start();

            OpenObserveLogstashAppender appender = new OpenObserveLogstashAppender(
                openObserveUrl, 
                username, 
                password, 
                encoder
            );
            appender.setContext(context);
            appender.setName("OPEN_OBSERVE_LOGSTASH");
            appender.start();

            Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.addAppender(appender);

        } catch (Exception e) {
            System.err.println("Failed to configure OpenObserve: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean isConnectionWorking(String targetUrl, String user, String pass) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            
            String auth = user + ":" + pass;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

            String payload = "[]"; 
            conn.getOutputStream().write(payload.getBytes());

            int code = conn.getResponseCode();
            System.out.println("OpenObserve Response Code: " + code);
            
            return code >= 200 && code < 500; 
        } catch (Exception e) {
            System.err.println("Connection Error: " + e.getMessage());
            return false;
        }
    }
	
}
