package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entities.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	public void testCreateCountry() {
		Country country = countryRepository.save(new Country("India","IND"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}

}
