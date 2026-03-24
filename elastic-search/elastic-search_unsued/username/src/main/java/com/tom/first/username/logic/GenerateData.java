package com.tom.first.username.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.username.model.User;
import com.tom.first.username.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenerateData {

	private final UserRepository usernameRepository;
	private final GenerateDataUtil dataUtil;
	
    @Transactional
    public User processUser() {
    	var gen = genUser();
    	usernameRepository.save(gen);
    	return gen;
    }
	
	private User genUser() {
		User username = new User();
		username.setName(dataUtil.generateUsername());
		username.setEmail(dataUtil.generateEmail());
		username.setPassword(dataUtil.generatePassword());
		return username;
	}
}
