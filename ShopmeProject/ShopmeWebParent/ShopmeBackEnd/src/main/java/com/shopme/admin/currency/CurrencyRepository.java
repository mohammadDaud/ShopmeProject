package com.shopme.admin.currency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

	public List<Currency> findAllByOrderByNameAsc();
}
