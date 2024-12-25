package com.shopme.site.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entities.Country;
import com.shopme.common.entities.State;
import com.shopme.common.entities.StateDTO;

@RestController
public class StateRestController {
	@Autowired
	private StateRepository repo;

	@GetMapping("/settings/list_state_by_country/{id}")
	public List<StateDTO> listAllStates(@PathVariable("id") Long id) {
		List<StateDTO> result = new ArrayList<>();
		List<State> listState = repo.findByCountryOrderByNameAsc(new Country(id));
		for (State s : listState) {
			result.add(new StateDTO(s.getId(), s.getName()));
		}
		return result;
	}

}
