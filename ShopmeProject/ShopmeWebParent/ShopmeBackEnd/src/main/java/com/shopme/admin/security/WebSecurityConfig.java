package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Bean
	public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
	@Bean
	public UserDetailsService userDetailsService() { return new ShopmeUserDetailsService(); }
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoauth = new DaoAuthenticationProvider();
		daoauth.setUserDetailsService(userDetailsService());
		daoauth.setPasswordEncoder(passwordEncoder());
		return daoauth;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception { web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/fontawesome/**", "/webjars/**"); }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/users/**").hasAuthority("Admin")
		.antMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editors")
		
		.antMatchers("/products/newproduct","/products/delete/**").hasAnyAuthority("Admin","Editors")
		.antMatchers("/products/saveproduct","/products/edit/**","/products/check_unique")
		.hasAnyAuthority("Admin","Editors","Salesperson")
		.antMatchers("/products","/products/","/products/view/**","/products/page/**")
		.hasAnyAuthority("Admin","Editors","Salesperson","Shipper")
		
		.antMatchers("/products/**").hasAnyAuthority("Admin","Editors")
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.usernameParameter("email")
		.permitAll()
		.and()
		.logout()
		.permitAll()
		.and()
		.rememberMe()
		.key("ShopMe_2021")
		.tokenValiditySeconds(7*24*60*60);
	}
}
