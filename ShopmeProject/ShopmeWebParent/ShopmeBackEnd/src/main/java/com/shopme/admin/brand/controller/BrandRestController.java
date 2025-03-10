package com.shopme.admin.brand.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.CategoryDTO;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.exception.BrandNotFoundRestException;
import com.shopme.common.entities.Brand;
import com.shopme.common.entities.Category;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Long id,@Param("name") String name) {
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoryByBrandId(@PathVariable("id") Long id) throws BrandNotFoundRestException {
		List<CategoryDTO> listcategory=new ArrayList<>();
		try {
			Brand brand = brandService.getBrandById(id);
			Set<Category> categories = brand.getCategories();
			for(Category category:categories) {
				CategoryDTO dto=new CategoryDTO(category.getId(), category.getName());
				listcategory.add(dto);
			}
			return listcategory;
		} catch (BrandNotFoundException e) {
			throw new  BrandNotFoundRestException();
		}
	}
}
