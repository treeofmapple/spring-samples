package com.tom.security.hash.logic.scheduled;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.tom.security.hash.security.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenCleanupJob implements Job {

	private final TokenRepository tokenRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("QUARTZ JOB STARTED: Cleaning up expired and revoke tokens...");
		int deletedCount = tokenRepository.deleteAllInvalidTokens();
		log.info("QUARTZ JOB FINISHED: Deleted {} tokens.", deletedCount);
	}

}
