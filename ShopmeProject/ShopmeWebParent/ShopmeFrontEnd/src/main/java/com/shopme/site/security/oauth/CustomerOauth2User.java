package com.shopme.site.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOauth2User implements OAuth2User{

	private String clientName;
	private String fullName;
	private OAuth2User oauth2user;
	
	public CustomerOauth2User(OAuth2User oauth2user,String clientName) {
		this.oauth2user = oauth2user;
		this.clientName = clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2user.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2user.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2user.getAttribute("name");
	}

	public String getEmail() {
		return oauth2user.getAttribute("email");
	}
	
	public String getFullName() {
		return fullName !=null ?fullName :oauth2user.getAttribute("name");
	}

	public String getClientName() {
		return clientName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
