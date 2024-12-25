package com.shopme.site.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.AuthenticationType;
import com.shopme.common.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("SELECT c FROM Customer c WHERE c.email=?1")
	public Customer findByEmail(String email);

	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);

	@Query("UPDATE Customer c SET c.enabled = true , c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Long id);

	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAuthenticationType(Long id, AuthenticationType type);

	public Customer findByResetPasswordToken(String token);
}
