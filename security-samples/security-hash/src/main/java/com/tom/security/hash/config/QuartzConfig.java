package com.tom.security.hash.config;

import java.time.Duration;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tom.security.hash.logic.scheduled.TokenCleanupJob;

@Configuration
public class QuartzConfig {

	@Value("${scheduled.token-cleanup:24h}") 
    private Duration tokenCleanupDuration;
	
	/*
	
	@Primary
    @Bean
    SchedulerFactoryBean primaryScheduler(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSchedulerName("PersistentScheduler");
        return factory;
    }
	
	@Bean
    SchedulerFactoryBean secondaryScheduler() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("InMemoryScheduler");
        return factory;
    }
	
	*/
	
	@Bean
	JobDetail tokenCleanupJobDetail() {
		return JobBuilder.newJob(TokenCleanupJob.class)
				.withIdentity("Token Cleanup Job", "security_jobs")
				.storeDurably()
				.withDescription("Deletes expired and revoked JWT tokens")
				.build();
	}
	
	@Bean
	Trigger tokenCleanupTrigger(JobDetail tokenCleanupJobDetail) {
		return TriggerBuilder.newTrigger()
				.forJob(tokenCleanupJobDetail)
				.withIdentity("Token Cleanup Trigger", "security_triggers")
				.startNow() // Starts on the server start
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMilliseconds(tokenCleanupDuration.toMillis()).repeatForever())
				.build();
	}
	
}
