package com.shopme.site.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final Long PRODUCTS_PER_PAGE = 10L;

	@Autowired
	private ProductRepository productRepository;

	public Page<Product> listByCategory(Long pageNum, Long categoryId) {
		String categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum.intValue() - 1, PRODUCTS_PER_PAGE.intValue());
		return productRepository.listByCategory(categoryId, categoryIDMatch, pageable);
	}

	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if(product == null) {
			throw new ProductNotFoundException("could not found any Product");
		}
		return product;
	}
}
