package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.Setting;
import com.shopme.common.entities.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
}
