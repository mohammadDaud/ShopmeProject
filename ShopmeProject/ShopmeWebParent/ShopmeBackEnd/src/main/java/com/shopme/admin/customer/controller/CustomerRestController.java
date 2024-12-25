package com.shopme.admin.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.customer.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("id") Long id,@Param("email") String email) {
		if(customerService.isEmailUnique(id, email)) {
			return "Ok";
		} else {
			return "Duplicated";
		}
	}
}
