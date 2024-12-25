package com.shopme.admin.customer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.customer.CustomerService;
import com.shopme.common.entities.Country;
import com.shopme.common.entities.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService service;

	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(model, 1L, "firstName", "asc", null);
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum") Long pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		long startCount = (pageNum - 1L) * CustomerService.CUSTOMERS_PER_PAGE + 1L;
		model.addAttribute("startCount", startCount);
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE + 1L;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("endCount", endCount);
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
			RedirectAttributes attributes) {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer id=" + id + " has been " + status;
		attributes.addFlashAttribute("message", message);
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomers(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomers(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			Customer customer = service.get(id);
			List<Country> listCountries = service.listAllCountries();
			model.addAttribute("pageTitle", "Edit Customer (ID=" + id + ")");
			model.addAttribute("customer", customer);
			model.addAttribute("listCountries", listCountries);
			return "customers/customer_form";
		} catch (CustomerNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/savecustomer")
	public String saveCustomer(Customer customer, RedirectAttributes attributes) throws IOException {
		service.saveCustomer(customer);
		attributes.addFlashAttribute("message", "Customer has been save successfully!!!!");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			service.deleteCustomer(id);
			attributes.addFlashAttribute("message", "Customer deleted successfully!!");
		} catch (CustomerNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";
	}
}
