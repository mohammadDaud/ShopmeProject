package com.shopme.site;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Country;
import com.shopme.common.entities.Customer;
import com.shopme.site.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomerfirst() {
		Long countryId = 2L;
		Country country = entityManager.find(Country.class, countryId);
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Mohammad");
		customer.setLastName("Daud");
		customer.setPassword("daud2020");
		customer.setEmail("daud@gmail.com");
		customer.setPhoneNumber("7895643215");
		customer.setAddressLine1("maqbool nagar jaipurwa gandhi nagar");
		customer.setCity("Basti");
		customer.setState("Utter Pradesh");
		customer.setPostalCode("272001");
		customer.setCreatedTime(new Date());

		Customer sevedCustomer = customerRepository.save(customer);
		assertThat(sevedCustomer).isNotNull();
		assertThat(sevedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomerSecond() {
		Long countryId = 2L;
		Country country = entityManager.find(Country.class, countryId);
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Abdul");
		customer.setLastName("Wadood");
		customer.setPassword("wadood2020");
		customer.setEmail("wadood@gmail.com");
		customer.setPhoneNumber("7895643215");
		customer.setAddressLine1("maqbool nagar jaipurwa gandhi nagar");
		customer.setCity("Basti");
		customer.setState("Utter Pradesh");
		customer.setPostalCode("272001");
		customer.setCreatedTime(new Date());

		Customer sevedCustomer = customerRepository.save(customer);
		assertThat(sevedCustomer).isNotNull();
		assertThat(sevedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAllCustomer() {
		Iterable<Customer> findAll = customerRepository.findAll();
		findAll.forEach(System.out::println);
		assertThat(findAll).hasSizeGreaterThan(1);
	}

	@Test
	public void updateAuthenticationType() {
		
	}
}
