package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entities.Role;
import com.shopme.common.entities.User;

public class ShopmeUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private User user;

	public ShopmeUserDetails() {
	}

	public ShopmeUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public String getFullname() {
		return this.user.getFirstname()+" "+this.user.getLastname();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstname(firstName);
	}
	public void setLastName(String lastName) {
		this.user.setLastname(lastName);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
}
