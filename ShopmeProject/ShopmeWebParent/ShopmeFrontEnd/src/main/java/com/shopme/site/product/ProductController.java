package com.shopme.site.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entities.Category;
import com.shopme.common.entities.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.site.category.CategoryService;

@Controller
public class ProductController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String category_alias, Model model) {
		return viewCategoryByPage(category_alias, 1L, model);
	}

	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") Long pageNum, Model model) {
		try {
			Category category = categoryService.getCategoryByAlias(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(category);
			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProducts.getContent();
			long startCount = (pageNum - 1L) * ProductService.PRODUCTS_PER_PAGE + 1L;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1L;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("category", category);
			return "product/products_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
	}

	@GetMapping("/p/{product_alias}")
	public String viewProductDetails(@PathVariable("product_alias") String alias, Model model) {
		try {
			Product product = productService.getProductByAlias(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}

	}
}
