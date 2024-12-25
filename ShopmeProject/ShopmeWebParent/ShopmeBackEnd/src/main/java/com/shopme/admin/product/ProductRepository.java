package com.shopme.admin.product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entities.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

	public Long countById(Long id);

	public Product findByName(String name);

	@Query("UPDATE Product p SET p.enabled=?2 WHERE p.id=?1")
	@Modifying
	public void updateEnabledStatus(Long id,boolean enabled);

}
