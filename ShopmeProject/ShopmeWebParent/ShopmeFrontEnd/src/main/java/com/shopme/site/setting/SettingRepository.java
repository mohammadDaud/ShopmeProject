package com.shopme.site.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Setting;
import com.shopme.common.entities.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 Or s.category = ?2")
	public List<Setting> findByTwoCategories(SettingCategory cateOne,SettingCategory cateTwo);
}
