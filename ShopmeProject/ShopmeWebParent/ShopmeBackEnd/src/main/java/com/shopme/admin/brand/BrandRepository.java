package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entities.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Long> {

	public Long countById(Long id);

	public Brand findByName(String name);
	
	@Query("SELECT NEW Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
}
