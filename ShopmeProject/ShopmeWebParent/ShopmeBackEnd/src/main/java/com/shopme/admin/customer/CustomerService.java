package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entities.Country;
import com.shopme.common.entities.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {
	public static final Long CUSTOMERS_PER_PAGE = 10L;

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Page<Customer> listByPage(Long pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum.intValue() - 1, CUSTOMERS_PER_PAGE.intValue(), sort);
		if (keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}
		return customerRepository.findAll(pageable);
	}

	public void updateCustomerEnabledStatus(Long id, boolean enabled) {
		customerRepository.updateEnabledStatus(id, enabled);
	}

	public Customer get(Long id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("could not found any customer with ID " + id);
		}
	}

	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(Long id, String email) {
		Customer existCustomer = customerRepository.findByEmail(email);
		if (existCustomer != null && existCustomer.getId() != id) {
			return false;
		}
		return true;
	}

	public void saveCustomer(Customer customerInForm) {
		Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
		customerRepository.save(customerInForm);
	}
	
	public void deleteCustomer(Long id) throws CustomerNotFoundException {
		Long count = customerRepository.countById(id);
		if(count == null || count==0) {
			throw new CustomerNotFoundException("could not found Any customer with id" + id);
		}
		customerRepository.deleteById(id);
	}
}
