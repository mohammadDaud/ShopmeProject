package com.shopme.site.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entities.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)" + " ORDER BY p.name ASC")
	public Page<Product> listByCategory(Long categoryId, String categoryIDMatch, Pageable pageable);

	public Product findByAlias(String alias);

}
