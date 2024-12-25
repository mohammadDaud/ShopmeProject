package com.shopme.admin.brand.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.export.BrandCsvExpoter;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entities.Brand;
import com.shopme.common.entities.Category;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, model);
	}

	@SuppressWarnings("static-access")
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Brand> page = brandService.listByPageBrands(pageNum);
		List<Brand> listBrands = page.getContent();
		long startCount = (pageNum - 1) * brandService.BRAND_PER_PAGE + 1;
		long endCount = startCount + brandService.BRAND_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		return "brands/brands";
	}

	@GetMapping("/brands/newbrand")
	public String addNewBrands(Model model) {
		List<Category> listCategories = categoryService.listCategoriesInForm();
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brand_form";
	}

	@PostMapping("/brands/savebrand")
	public String saveBrand(Brand brand, RedirectAttributes attributes,
			@RequestParam("fileImage") MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			brand.setLogo(fileName);
			Brand saveBrand = brandService.saveBrand(brand);
			String uploadDir = "../brand-logos/" + saveBrand.getId();
			FileUploadUtility.cleanDir(uploadDir);
			FileUploadUtility.saveFile(uploadDir, fileName, file);
		} else {
			if (brand.getLogo().isEmpty())
				brand.setLogo(null);
			brandService.saveBrand(brand);
		}

		attributes.addFlashAttribute("message", "brand has been save successfully!!!!");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrands(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			List<Category> listCategories = categoryService.listCategoriesInForm();
			Brand brand = brandService.getBrandById(id);
			model.addAttribute("pageTitle", "Edit Brand (ID=" + id + ")");
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			return "brands/brand_form";
		} catch (BrandNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			brandService.delete(id);
			attributes.addFlashAttribute("message", "brand deleted successfully!!");
		} catch (BrandNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";
	}

	@GetMapping("/brands/export/csv")
	public void exportCsv(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listBrands();
		BrandCsvExpoter exporter = new BrandCsvExpoter();
		exporter.export(listBrands, response);
	}
}
