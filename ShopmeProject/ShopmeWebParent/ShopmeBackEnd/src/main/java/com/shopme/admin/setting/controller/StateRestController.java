package com.shopme.admin.setting.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entities.Country;
import com.shopme.common.entities.State;
import com.shopme.common.entities.StateDTO;

@RestController
public class StateRestController {
	@Autowired
	private StateRepository repo;

	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listAllStates(@PathVariable("id") Long id) {
		List<StateDTO> result = new ArrayList<>();
		List<State> listState = repo.findByCountryOrderByNameAsc(new Country(id));
		for (State s : listState) {
			result.add(new StateDTO(s.getId(), s.getName()));
		}
		return result;
	}

	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State saveState = repo.save(state);
		return String.valueOf(saveState.getId());
	}

	@GetMapping("/states/delete/{id}")
	public void deleteStates(@PathVariable("id") Long id) {
		repo.deleteById(id);
	}

}
