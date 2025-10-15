package com.tom.auth.monolithic.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tom.auth.monolithic.security.SecurityUtils;
import com.tom.auth.monolithic.user.dto.admin.AdminPasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.admin.DeleteListResponse;
import com.tom.auth.monolithic.user.dto.admin.DeleteUsersRequest;
import com.tom.auth.monolithic.user.dto.admin.PageAdminLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.admin.PageAdminUserResponse;
import com.tom.auth.monolithic.user.dto.admin.PageLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.admin.UserAdminResponse;
import com.tom.auth.monolithic.user.dto.authentication.PasswordAuthenticationRequest;
import com.tom.auth.monolithic.user.dto.user.PasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.user.UpdateAccountRequest;
import com.tom.auth.monolithic.user.mapper.AdminMapper;
import com.tom.auth.monolithic.user.mapper.LoginMapper;
import com.tom.auth.monolithic.user.mapper.UserMapper;
import com.tom.auth.monolithic.user.model.LoginHistory;
import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.model.enums.Role;
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
public class AdminService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final LoginHistoryRepository loginRepository;
	private final UserMapper userMapper;
	private final LoginMapper loginMapper;
	private final AdminMapper adminMapper;
	private final UserUtils userUtils;
	private final TokenUtils tokenUtils;
	private final SecurityUtils securityUtils;
	
	public UserAdminResponse getCurrentUserAdmin() {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		return adminMapper.toResponse(user);
	}
	
	@Transactional(readOnly = true)
	public PageAdminUserResponse searchUserByParamsAdmin(int page, String username, String email, Integer age, String roles, Boolean accountLocked) {
		
        Role roleEnum = null;
        if (StringUtils.hasText(roles)) {
            try {
                roleEnum = Role.valueOf(roles.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role value: '" + roles + "'. Allowed values are: " + Arrays.toString(Role.values()));
            }
        }
		
		Specification<User> spec = UserSpecification.findByCriteria(username, email, age, roleEnum, accountLocked);
		log.info("IP: {}, admin is finding params: {}", securityUtils.getRequestingClientIp(), spec);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<User> users = userRepository.findAll(spec, pageable);
		return adminMapper.toResponse(users);
	}
	
	@Transactional(readOnly = true)
	public UserAdminResponse searchUserById(UUID query) {
		var user = userUtils.findUserByUserId(query);
		return adminMapper.toResponse(user);
	}

	@Transactional(readOnly = true)
	public PageAdminLoginHistoryResponse seelastUserConnected(int page) {
		log.info("IP: {}, admin is requesting to see last users connected.", securityUtils.getRequestingClientIp());
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<LoginHistory> loginHistoryPage = loginRepository.findByOrderByLoginTimeDesc(pageable);
		return loginMapper.toAdminResponse(loginHistoryPage);
	}

	@Transactional(readOnly = true)
	public PageLoginHistoryResponse viewAnUserLoginHistory(int page, String identifier) {
		log.info("IP: {}, admin is requesting to see the user: {} login history.", securityUtils.getRequestingClientIp(), identifier);
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<LoginHistory> loginHistoryPage = loginRepository.findByUserIdOrderByLoginTimeDesc(user.getId(), pageable);
		return loginMapper.toResponse(loginHistoryPage); 
	}
	
	@Transactional
	public void changeAnUserPassword(String identifier, AdminPasswordUpdateRequest request) {
		log.info("IP: {}, admin is changing password for user: {}", securityUtils.getRequestingClientIp(), identifier);
		
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		userRepository.save(user);
		log.info("IP: {}, admin changed successfully the password of user: {}", securityUtils.getRequestingClientIp(), user.getUsername());
	}
	
	@Transactional
	public void changeAdminPassword(PasswordUpdateRequest request) {
		var user = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, admin {} is changing their password.", securityUtils.getRequestingClientIp(), user.getUsername());
		
		if (!passwordEncoder.matches(request.currentpassword(), user.getPassword())) {
			throw new BadCredentialsException("Password not matches");
		}
		
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new BadCredentialsException("Passwords are not the same");
		}
		
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		userRepository.save(user);
		
		log.info("IP: {}, admin changed successfully the password of user: {}", securityUtils.getRequestingClientIp(), user.getUsername());
	}

	@Transactional
	public UserAdminResponse updateAnUser(String identifier, UpdateAccountRequest request) {
		log.info("IP: {}, admin is updating user: {}", securityUtils.getRequestingClientIp(), identifier);
		
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		
		userUtils.checkIfUsernameIsTakenByAnotherUser(user, request.username());
		userUtils.checkIfEmailIsTakenByAnotherUser(user, request.email());
		
		var newUser = userMapper.updateUser(user, request);
		var userEdited = userRepository.save(newUser);
		
		log.info("IP: {}, changed the info of the user: {}, to user: {}", securityUtils.getRequestingClientIp(), user.getUsername(), userEdited.getUsername());
		return adminMapper.toResponse(userEdited);
	}
	
	@Transactional
	public UserAdminResponse updateUserAdmin(UpdateAccountRequest request) {
		log.info("IP: {}, admin is updating their own admin account.", securityUtils.getRequestingClientIp());
		
		var user = securityUtils.getAuthenticatedUserOrThrow();
		log.info("IP: {}, is being edited: {}", securityUtils.getRequestingClientIp(), user.getUsername());
		
		userUtils.checkIfUsernameIsTakenByAnotherUser(user, request.username());
		userUtils.checkIfEmailIsTakenByAnotherUser(user, request.email());
		
		var newUser = userMapper.updateUser(user, request);
		var userEdited = userRepository.save(newUser);
		
		log.info("IP: {}, changed the info of the user: {}, to user: {}", securityUtils.getRequestingClientIp(), user.getUsername(), userEdited.getUsername());
		return adminMapper.toResponse(userEdited);
	}
	
	@Transactional
	public void logoutEveryone(PasswordAuthenticationRequest request) {
		log.info("IP: {}, admin is logging out all users.", securityUtils.getRequestingClientIp());

		User adminUser = securityUtils.getAuthenticatedUserOrThrow();
		
	    if (!passwordEncoder.matches(request.password(), adminUser.getPassword())) {
	        log.warn("SECURITY: Failed attempt to log out all users due to incorrect admin password.");
	        throw new BadCredentialsException("Incorrect admin password provided.");
	    }
	    
	    List<User> usersToLogout = userRepository.findAllByRoleNot(Role.ADMIN);
	    if (usersToLogout.isEmpty()) {
	        log.info("No non-admin users found to log out.");
	        return;
	    }
	    
	    int count = 0;
	    for (User user : usersToLogout) {
	        tokenUtils.revokeUserAllTokens(user);
	        count++;
	    }
		
	    log.info("Successfully revoked all tokens for {} non-admin users.", count);
	}

	@Transactional
	public UserAdminResponse banUser(String identifier) {
		log.info("IP: {}, admin is banning user: {}", securityUtils.getRequestingClientIp(), identifier);
		
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		user.setAccountNonLocked(false);
		userRepository.save(user);
		return adminMapper.toResponse(user);
	}

	@Transactional
	public UserAdminResponse unbanUser(String identifier) {
		log.info("IP: {}, admin is banning user: {}", securityUtils.getRequestingClientIp(), identifier);
		
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		user.setAccountNonLocked(true);
		userRepository.save(user);
		return adminMapper.toResponse(user);
	}
	
	@Transactional
	public void deleteAnUser(String identifier) {
		log.info("IP: {}, admin is deleting user: {}", securityUtils.getRequestingClientIp(), identifier);
		
		var user = userUtils.findUserByIdOrIdentifier(identifier);
		userRepository.deleteById(user.getId());
	}
	
	@Transactional
	public DeleteListResponse deleteUsers(DeleteUsersRequest request) {
		log.info("IP: {}, admin is deleting users with IDs: {}", securityUtils.getRequestingClientIp(), request.userIds());
		
		var adminUser = securityUtils.getAuthenticatedUserOrThrow();
		
		if (!passwordEncoder.matches(request.password(), adminUser.getPassword())) {
	        log.warn("SECURITY: Failed attempt to delete users due to incorrect admin password.");
			throw new BadCredentialsException("Password not matches");
		}
		
	    List<UUID> idsToDelete = request.userIds().stream()
	    		.filter(id -> !id.equals(adminUser.getId()))
	    		.collect(Collectors.toList());
	    
	    if (idsToDelete.isEmpty()) {
	    	log.info("No users to delete after filtering for admin's own ID.");
	    	return new DeleteListResponse(List.of());
	    }
	    
	    List<User> usersToDelete = userRepository.findAllByIdIn(idsToDelete);
	    
	    if(usersToDelete.isEmpty()) {
	    	log.info("None of the provided user IDs were found in the database.");
	    	return new DeleteListResponse(List.of());
	    }
	    
	    userRepository.deleteAll(usersToDelete);

	    List<UUID> deletedIds = usersToDelete.stream()
	    		.map(User::getId)
	    		.collect(Collectors.toList());
	    
		return adminMapper.toResponse(deletedIds);
	}
	
	//TODO: Make a future implementation
	@API(status = Status.EXPERIMENTAL)
	@Transactional
	public void changeAnUserRole(UUID userId, Role roles) {
		log.info("IP: {}, admin is changing role of the user: {} to: {}", securityUtils.getRequestingClientIp(), userId, roles);
	}
	
}
