package com.shopme.admin.setting.controllertest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entities.Country;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	CountryRepository countryRepository;

	@Test
	@WithMockUser(username = "daud@gmail.com", password = "daud2020", roles = "Admin")
	public void testlistCountries() throws Exception {
		String url = "/countries/list";
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String asString = result.getResponse().getContentAsString();
		Country[] countries = mapper.readValue(asString, Country[].class);
		assertThat(countries).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "daud@gmail.com", password = "daud2020", roles = "Admin")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "America";
		Country country = new Country(countryName, "USA");
		MvcResult mvcResult = mockMvc.perform(
				post(url).contentType("application/json").content(mapper.writeValueAsString(country)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		Long countryId = Long.parseLong(response);
		System.out.println("Country ID : " + response);
		Country savedCountry = countryRepository.findById(countryId).get();
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}

	@Test
	@WithMockUser(username = "daud@gmail.com", password = "daud2020", roles = "Admin")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		Long countryId = 3L;
		String countryName = "Turkey";
		String countryCode = "TR";
		Country country = new Country(countryId, countryName, countryCode);
		mockMvc.perform(post(url).contentType("application/json").content(mapper.writeValueAsString(country)).with(csrf()))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(String.valueOf(countryId)));
	
		Country savedCountry = countryRepository.findById(countryId).get();
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	}
	
	@Test
	@WithMockUser(username = "daud@gmail.com", password = "daud2020", roles = "Admin")
	public void testDeleteCountry() throws Exception {
		Long countryId = 5L;
		String url = "/countries/delete/"+countryId;
		mockMvc.perform(get(url)).andExpect(status().isOk());
		
		Optional<Country> findById = countryRepository.findById(countryId);
		assertThat(findById).isNotPresent();
	}
}
