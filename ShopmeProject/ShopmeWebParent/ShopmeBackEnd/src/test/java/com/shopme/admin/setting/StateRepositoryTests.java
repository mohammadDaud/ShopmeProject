package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entities.Country;
import com.shopme.common.entities.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateStates() {
		Long countryId=1L;
		Country country = entityManager.find(Country.class, countryId);
		State state = stateRepository.save(new State("Shanghai", country));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
}
