package com.shopme.admin.category.controller;

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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.export.CategoryCsvExpoter;
import com.shopme.common.entities.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return listByPage(1, model);
	}

	@SuppressWarnings("static-access")
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Category> page = categoryService.listByPageCategories(pageNum);
		List<Category> listCategories = page.getContent();
		long startCount = (pageNum - 1) * categoryService.CATEGORY_PER_PAGE + 1;
		long endCount = startCount + categoryService.CATEGORY_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		return "category/categories";
	}
	
	@GetMapping("/categories/newcategory")
	public String addNewCategories(Model model) {
		List<Category> listCategoriesParent = categoryService.listCategoriesInForm();
		Category category = new Category();
		category.setEnabled(true);
		model.addAttribute("category", category);
		model.addAttribute("listCategoriesParent", listCategoriesParent);
		model.addAttribute("pageTitle", "Create New Category");
		return "category/category_form";
	}

	@PostMapping("/categories/savecategory")
	public String saveCategory(Category category, RedirectAttributes attributes, @RequestParam("fileImage") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			category.setImage(fileName);
			Category saveCategory = categoryService.saveCategory(category);
			String uploadDir = "../category-images/" + saveCategory.getId();
			FileUploadUtility.cleanDir(uploadDir);
			FileUploadUtility.saveFile(uploadDir, fileName, file);
		} else {
			if (category.getImage().isEmpty())
				category.setImage(null);
			categoryService.saveCategory(category);
		}

		attributes.addFlashAttribute("message", "category has been save successfully!!!!");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			List<Category> listCategoriesParent = categoryService.listCategoriesInForm();
			Category category = categoryService.getCategoryById(id);
			model.addAttribute("pageTitle", "Edit Category (ID=" + id + ")");
			model.addAttribute("category", category);
			model.addAttribute("listCategoriesParent", listCategoriesParent);
			return "category/category_form";
		} catch (CategoryNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			categoryService.delete(id);
			attributes.addFlashAttribute("message", "category deleted successfully!!");
		} catch (CategoryNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String editCategory(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
			RedirectAttributes attributes) {
		categoryService.updateStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category id=" + id + " has been " + status;
		attributes.addFlashAttribute("message", message);
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportCsv(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategories();
		CategoryCsvExpoter exporter=new CategoryCsvExpoter();
		exporter.export(listCategories, response);
	}
}
