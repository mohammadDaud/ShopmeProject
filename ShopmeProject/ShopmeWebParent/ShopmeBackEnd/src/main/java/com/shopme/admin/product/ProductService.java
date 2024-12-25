package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {

	public static final int PRODUCT_PER_PAGE = 4;

	@Autowired
	private ProductRepository productRepository;

	public List<Product> listProducts() {
		return (List<Product>) productRepository.findAll();
	}

	public Page<Product> listByPageProducts(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
		return productRepository.findAll(pageable);
	}

	public Product saveProduct(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			product.setAlias(product.getName().replace(" ", "-").toLowerCase());
		} else {
			product.setAlias(product.getAlias().replace(" ", "-").toLowerCase());
		}
		product.setUpdatedTime(new Date());
		Product save = productRepository.save(product);
		return save;
	}

	public Product getProductById(Long id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any Product!!");
		}
	}

	public void delete(Long id) throws ProductNotFoundException {
		Long countById = productRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any Product!!");
		}
		productRepository.deleteById(id);
	}

	public void updateStatus(Long id, boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}

	public String checkUnique(Long id, String name) {
		boolean iscreatedNew = (id == null || id == 0);

		Product productByName = productRepository.findByName(name);
		if (iscreatedNew) {
			if (productByName != null)
				return "DuplicateName";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "DuplicateName";
			}
		}
		return "OK";
	}

	public void saveProductPrice(Product productInForm) {
		Product productInDB = productRepository.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());
		productRepository.save(productInDB);
	}

}
