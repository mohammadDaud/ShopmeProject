package com.shopme.admin.product.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.product.export.ProductCsvExporter;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entities.Brand;
import com.shopme.common.entities.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model);
	}

	@SuppressWarnings("static-access")
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Product> page = productService.listByPageProducts(pageNum);
		List<Product> listProducts = page.getContent();
		long startCount = (pageNum - 1) * productService.PRODUCT_PER_PAGE + 1;
		long endCount = startCount + productService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}

	@GetMapping("/products/newproduct")
	public String addNewProducts(Model model) {
		List<Brand> listBrands = brandService.listBrands();
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("numberOfExtraImage", 0);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		return "products/product_form";
	}

	@PostMapping("/products/saveproduct")
	public String saveProduct(Product product, RedirectAttributes attributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImage,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImage,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {
		if (loggedUser.hasRole("Salesperson")) {
			productService.saveProductPrice(product);
			attributes.addFlashAttribute("message", "product has been save successfully!!!!");
			return "redirect:/products";
		}
		ProductSaveHelper.setMainImageName(mainImage, product);
		ProductSaveHelper.setExistingExtraImage(imageIDs, imageNames, product);
		ProductSaveHelper.setExtraImageName(extraImage, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
		Product saveProduct = productService.saveProduct(product);
		ProductSaveHelper.deleteExtraImagesWeredExistInFolder(product);
		ProductSaveHelper.saveUploadedImage(mainImage, extraImage, saveProduct);
		attributes.addFlashAttribute("message", "product has been save successfully!!!!");
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProducts(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			List<Brand> listBrands = brandService.listBrands();
			Product product = productService.getProductById(id);
			Integer numberOfExtraImage = product.getImages().size();
			model.addAttribute("pageTitle", "Edit Product (ID=" + id + ")");
			model.addAttribute("product", product);
			model.addAttribute("numberOfExtraImage", numberOfExtraImage);
			model.addAttribute("listBrands", listBrands);
			return "products/product_form";
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/view/{id}")
	public String viewProducts(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			Product product = productService.getProductById(id);
			model.addAttribute("pageTitle", "View Product (ID=" + id + ")");
			model.addAttribute("product", product);
			return "products/product_detail_modal";
		} catch (ProductNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productMainDir = "../product-images/" + id;
			productService.delete(id);
			FileUploadUtility.cleanDir(productExtraImagesDir);
			FileUploadUtility.cleanDir(productMainDir);
			FileUploadUtility.removeDir(productMainDir);
			attributes.addFlashAttribute("message", "Product deleted successfully!!");
		} catch (ProductNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String editproductStatus(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
			RedirectAttributes attributes) {
		productService.updateStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product id=" + id + " has been " + status;
		attributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping("/products/export/csv")
	public void exportCsv(HttpServletResponse response) throws IOException {
		List<Product> listProducts = productService.listProducts();
		ProductCsvExporter exporter = new ProductCsvExporter();
		exporter.export(listProducts, response);
	}

}
