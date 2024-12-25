package com.shopme.admin.setting.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entities.Currency;
import com.shopme.common.entities.Setting;

@Controller
public class SettingController {

	@Autowired
	private SettingService service;

	@Autowired
	private CurrencyRepository currencyRepository;

	@GetMapping("/settings")
	public String listAllSetting(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
		model.addAttribute("listCurrencies", listCurrencies);
		model.addAttribute("pageTitle", "Site Settings");
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile file, HttpServletRequest request,
			RedirectAttributes re) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSetting();
		saveSiteLogo(file, settingBag);
		saveGeneralSetting(request, settingBag);
		updateSettingValuesFromForm(request, settingBag.list());
		re.addFlashAttribute("message", "General Settings have been saved.");
		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes re) throws IOException {
		List<Setting> serverSettings = service.listMailServerSettings();
		updateSettingValuesFromForm(request, serverSettings);
		re.addFlashAttribute("message", "Mail Server Settings have been saved.");
		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplatesSettings(HttpServletRequest request, RedirectAttributes re) throws IOException {
		List<Setting> mailTemplatesSettings = service.listMailTemplatesSettings();
		updateSettingValuesFromForm(request, mailTemplatesSettings);
		re.addFlashAttribute("message", "Mail Template Settings have been saved.");
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile file, GeneralSettingBag settingBag) throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			String uploadDir = "../site-logo/";
			FileUploadUtility.cleanDir(uploadDir);
			FileUploadUtility.saveFile(uploadDir, fileName, file);
		}
	}

	private void saveGeneralSetting(HttpServletRequest request, GeneralSettingBag settingBag) {
		Long currencyId = Long.parseLong(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByResult = currencyRepository.findById(currencyId);
		if (findByResult.isPresent()) {
			Currency currency = findByResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		service.saveSettings(listSettings);
	}

}
