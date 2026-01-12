package com.tom.service.shortener.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tom.service.shortener.model.URL;

@Repository
public interface URLRepository extends JpaRepository<URL, UUID> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM URL u WHERE u.expirationTime <= :expirationTime")
    int deleteByExpirationTimeBefore(ZonedDateTime expirationTime);
	
	@Transactional(readOnly = true)
    Optional<URL> findByShortUrl(String shortUrl);
	
	@Transactional(readOnly = true)
	Optional<URL> findByOriginalUrl(String originalUrl);

	@Transactional(readOnly = true)
	boolean existsByShortUrl(String shortUrl);
	
	@Transactional(readOnly = true)
	boolean existsByOriginalUrl(String originalUrl);
	
}
