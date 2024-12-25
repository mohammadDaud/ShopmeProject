package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Setting;
import com.shopme.common.entities.SettingCategory;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired
	private SettingRepository repository;

	@Test
	public void testCreateGeneralSettings() {
		// Setting siteName = new Setting("SITE_NAME", "Shopme",
		// SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopme Ltd.", SettingCategory.GENERAL);
		Iterable<Setting> saveAll = repository.saveAll(List.of(siteLogo, copyright));
		saveAll.forEach(setting -> {
			System.out.println(setting.getKey());
			assertThat(setting).isNotNull();
		});
		/*
		 * assertThat(saveAll.).isNotNull();
		 * assertThat(savedSetting.getKey()).isNotEmpty();
		 */
	}

	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENY);
		Setting currencySymbol = new Setting("CURRENCY_SYMBOL", "Rs", SettingCategory.CURRENY);
		Setting currencySymbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENY);
		Iterable<Setting> saveAll = repository.saveAll(List.of(currencyId, currencySymbol, currencySymbolPosition,
				decimalPointType, decimalDigits, thousandsPointType));
		saveAll.forEach(setting -> {
			System.out.println(setting.getKey());
			assertThat(setting).isNotNull();
		});
	}
}
