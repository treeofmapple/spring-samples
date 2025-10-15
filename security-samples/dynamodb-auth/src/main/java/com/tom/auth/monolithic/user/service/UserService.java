package com.tom.auth.monolithic.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.auth.monolithic.common.ResourceUtils;
import com.tom.auth.monolithic.exception.NotFoundException;
import com.tom.auth.monolithic.security.SecurityUtils;
import com.tom.auth.monolithic.user.dto.admin.PageLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.user.DeleteAccountRequest;
import com.tom.auth.monolithic.user.dto.user.PageUserResponse;
import com.tom.auth.monolithic.user.dto.user.PasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.user.UpdateAccountRequest;
import com.tom.auth.monolithic.user.dto.user.UserExport;
import com.tom.auth.monolithic.user.dto.user.UserResponse;
import com.tom.auth.monolithic.user.mapper.LoginMapper;
import com.tom.auth.monolithic.user.mapper.UserMapper;
import com.tom.auth.monolithic.user.model.LoginHistory;
import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.repository.LoginHistoryRepository;
import com.tom.auth.monolithic.user.repository.UserRepository;
import com.tom.auth.monolithic.user.repository.UserSpecification;
import com.tom.auth.monolithic.user.service.utils.TokenUtils;
import com.tom.auth.monolithic.user.service.utils.UserUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final LoginHistoryRepository loginRepository;
	private final UserMapper userMapper;
	private final LoginMapper loginMapper;
	private final UserUtils userUtils;
	private final TokenUtils tokenUtils;
	private final ResourceUtils resourceUtils;
	private final SecurityUtils securityUtils;
	
	public UserResponse getCurrentUser() {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		return userMapper.toResponse(user);
	}
	
	public PageLoginHistoryResponse viewCurrentUserLoginHistory(int page) {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<LoginHistory> loginHistory = loginRepository.findByUserIdOrderByLoginTimeDesc(user.getId(), pageable);
		return loginMapper.toResponse(loginHistory);
	}
	
	@Transactional(readOnly = true)
	public PageUserResponse searchUserByParams(int page, String username, String email, Integer age) {
		Specification<User> spec = UserSpecification.findByCriteria(username, email, age);
		log.info("IP: {}, is finding params: {}", securityUtils.getRequestingClientIp());

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<User> users = userRepository.findAll(spec, pageable);
		return userMapper.toResponse(users);
	}
	
	@Transactional(readOnly = true)
	public UserResponse searchUserById(UUID query) {
		var user = userUtils.findUserByUserId(query);
		return userMapper.toResponse(user);
	}
	
	@Transactional(readOnly = true)
	public UserExport exportMyUserData() {
		var detachedUser = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, user: {}, is requesting their data.", securityUtils.getRequestingClientIp(), detachedUser.getUsername());
	    var user = userRepository.findById(detachedUser.getId())
	            .orElseThrow(() -> new NotFoundException("Authenticated user not found in database."));

		var userData = userMapper.dataExporation(user);
		byte[] data = resourceUtils.bytesToJson(userData);
		
	    String jsonFileName = "user_data_" + user.getUsername() + ".json";
	    String zipFileName = "user_data_" + user.getUsername() + ".zip";
		byte[] zipBytes = resourceUtils.resourceToBytes(data, jsonFileName);
		
		Resource resource = new ByteArrayResource(zipBytes);
		return new UserExport(resource, zipFileName, zipBytes.length);
	}

	@Transactional
	public UserResponse updateUser(UpdateAccountRequest request) {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, is being edited: {}", securityUtils.getRequestingClientIp(), user.getUsername());
	    boolean isSameUsername = request.username().equals(user.getUsername());
	    boolean isSameEmail = request.email().equals(user.getEmail());
	    
	    if (isSameUsername && isSameEmail) {
	        log.info("User {} submitted an update with no data changes. Skipping update.", user.getUsername());
	        return userMapper.toResponse(user);
	    }
		
		userUtils.checkIfUsernameIsTakenByAnotherUser(user, request.username());
		userUtils.checkIfEmailIsTakenByAnotherUser(user, request.email());
		
		var newUser = userMapper.updateUser(user, request);
		var userEdited = userRepository.save(newUser);
		
		log.info("IP: {}, changed the info of the user: {}, to user: {}", securityUtils.getRequestingClientIp(), user.getUsername(), userEdited.getUsername());
		return userMapper.toResponse(userEdited);
	}
	
	@Transactional
	public void changeUserPassword(PasswordUpdateRequest request) {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, is changing password of user: {}", securityUtils.getRequestingClientIp(), user.getUsername());
		
		if (!passwordEncoder.matches(request.currentpassword(), user.getPassword())) {
			throw new BadCredentialsException("Password not matches");
		}
		
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new BadCredentialsException("Passwords are not the same");
		}
		
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		userRepository.save(user);
		log.info("IP: {}, changed successfully the password of user: {}", securityUtils.getRequestingClientIp(), user.getUsername());
	}
	
	
	// Token auth
	@Transactional
	public void deleteMyAccount(DeleteAccountRequest request) {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, is deleting user: {}", securityUtils.getRequestingClientIp(), user.getUsername());
		
		if(!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect password provided for account deletion.");
		}

		securityUtils.invalidateUserSession();
	    tokenUtils.revokeUserAllTokens(user);
		userRepository.deleteById(user.getId());
		log.info("The user {} has deleted their account", user.getUsername());
	}
	
}
