package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {

	public List<Country> findAllByOrderByNameAsc();
}
