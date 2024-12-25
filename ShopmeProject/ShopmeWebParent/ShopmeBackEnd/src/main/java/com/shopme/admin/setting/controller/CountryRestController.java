package com.shopme.admin.setting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entities.Country;

@RestController
public class CountryRestController {

	@Autowired
	private CountryRepository repo;

	@GetMapping("/countries/list")
	public List<Country> listAllCountry() {
		return repo.findAllByOrderByNameAsc();
	}

	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		Country savedCountry = repo.save(country);
		return String.valueOf(savedCountry.getId());
	}
	
	@GetMapping("/countries/delete/{id}")
	public void deleteCountry(@PathVariable("id") Long id) {
		repo.deleteById(id);
	}
}
