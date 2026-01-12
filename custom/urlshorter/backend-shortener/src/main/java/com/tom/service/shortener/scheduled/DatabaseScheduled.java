package com.tom.service.shortener.scheduled;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.tom.service.shortener.common.DurationUtils;
import com.tom.service.shortener.repository.URLRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class DatabaseScheduled {

	@Value("${application.scheduled.database.expired-time:1H}")
	private String durationStr;
	
	private final URLRepository repository;
	private final TaskScheduler scheduler;
	
    @PostConstruct
    public void checkConnectionAtStartup() {
    	Duration rate = DurationUtils.parseDuration(durationStr);
    	scheduler.scheduleAtFixedRate(this::cleanExpiredURL, rate);
    }
	
	private void cleanExpiredURL() { 
		log.info("Starting cleanup for expired URL's");
		int deletedCount = repository.deleteByExpirationTimeBefore(ZonedDateTime.now());
		log.info("Expired URLs cleanup complete. Total deleted: {}", deletedCount);
	}
	
}
