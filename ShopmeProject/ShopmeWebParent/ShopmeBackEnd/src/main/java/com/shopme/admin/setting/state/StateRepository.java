package com.shopme.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Country;
import com.shopme.common.entities.State;

public interface StateRepository extends CrudRepository<State, Long> {

	public List<State> findByCountryOrderByNameAsc(Country country);
}
