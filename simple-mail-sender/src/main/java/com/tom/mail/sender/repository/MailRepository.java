package com.tom.mail.sender.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tom.mail.sender.model.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, UUID>, JpaSpecificationExecutor<Mail> {

	boolean existsByTitle(String title);
	
}
