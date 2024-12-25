package com.shopme.site.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService service;

	@PostMapping("/customers/check_unique_email")
	public String checkUniqueEmail(@Param("email") String email) {
		return service.isUniqueEmail(email) ? "Ok" : "Duplicated";

	}
}
