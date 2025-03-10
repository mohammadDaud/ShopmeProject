package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entities.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

	public Long countById(Long id);

	@Query("UPDATE Category c SET c.enabled=?2 WHERE c.id=?1")
	@Modifying
	public void updateEnabledStatus(Long id,boolean enabled);
	
	public Category findByName(String name);
	public Category findByAlias(String alias);
	@Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
	public List<Category> findRootCategory();

}
