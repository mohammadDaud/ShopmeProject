package com.shopme.site.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entities.AuthenticationType;
import com.shopme.common.entities.Customer;
import com.shopme.site.customer.CustomerService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOauth2User oauth2User = (CustomerOauth2User) authentication.getPrincipal();
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String code = request.getLocale().getCountry();
		String clientName = oauth2User.getClientName();
		AuthenticationType authenticationType = getAuthenticationType(clientName);
		
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponAuth2Login(name, email, code,authenticationType);
		} else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private AuthenticationType getAuthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
	}

}
