package com.shopme.site.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Category;

public interface CategoryRepositry extends CrudRepository<Category, Long> {

	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();

	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1 ORDER BY c.name ASC")
	public Category findByAliasEnabled(String alias);
}
