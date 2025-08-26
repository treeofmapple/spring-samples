package com.tom.auth.monolithic.user.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tom.auth.monolithic.model.Auditable;
import com.tom.auth.monolithic.user.model.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tokens"})
@Table(name = "users", indexes = {
		@Index(name = "idx_user_username", columnList = "username"),
		@Index(name = "idx_user_email", columnList = "email"),
})
public class User extends Auditable implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "username", 
			length = 40,
			unique = true, 
			updatable = true, 
			nullable = false)
	private String username;

	@Column(name = "email", 
			unique = true, 
			updatable = true, 
			nullable = false)
	private String email;
	
	@Column(name = "age", 
			unique = false, 
			updatable = true, 
			nullable = true)
	private Integer age;
	
	@Column(name = "password", 
			unique = false, 
			updatable = true, 
			nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role",
			nullable = false)
	private Role role;
	
	@OneToMany(mappedBy = "user", 
			cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<Token> tokens;

	@OneToMany(mappedBy = "user", 
			cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<LoginHistory> loginHistories;
	
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "account_enabled", nullable = false)
    private boolean enabled = true;

    public String getIdentifier() {
	    return this.id.toString();
	}
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
}
