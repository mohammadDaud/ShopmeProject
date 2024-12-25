package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.Setting;
import com.shopme.common.entities.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;

	public List<Setting> listAllSettings() {
		return (List<Setting>) settingRepository.findAll();
	}

	public GeneralSettingBag getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();
		List<Setting> generalSetting = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySetting = settingRepository.findByCategory(SettingCategory.CURRENY);
		settings.addAll(generalSetting);
		settings.addAll(currencySetting);
		return new GeneralSettingBag(settings);
	}

	public void saveSettings(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}

	public List<Setting> listMailServerSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> listMailTemplatesSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
}
