package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.export.UserCsvExpoter;
import com.shopme.admin.user.export.UserExcelExpoter;
import com.shopme.admin.user.export.UserPDFExpoter;
import com.shopme.common.entities.Role;
import com.shopme.common.entities.User;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model);
	}

	@SuppressWarnings("static-access")
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<User> page = service.listByPageUsers(pageNum);
		List<User> list = page.getContent();
		long startCount = (pageNum - 1) * service.USERS_PER_PAGE + 1;
		long endCount = startCount + service.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", list);
		return "users/users";
	}

	@GetMapping("/users/newuser")
	public String addNewUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}

	@PostMapping("/users/saveuser")
	public String saveUser(User user, RedirectAttributes attributes, @RequestParam("image") MultipartFile file)
			throws IOException {
		if (!file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			user.setPhoto(fileName);
			User saveUser = service.saveUser(user);
			String uploadDir = "user-photo/" + saveUser.getId();
			FileUploadUtility.cleanDir(uploadDir);
			FileUploadUtility.saveFile(uploadDir, fileName, file);
		} else {
			if (user.getPhoto().isEmpty())
				user.setPhoto(null);
			service.saveUser(user);
		}

		attributes.addFlashAttribute("message", "user has been save successfully!!!!");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			User user = service.getById(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("pageTitle", "Edit User (ID=" + id + ")");
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			return "users/user_form";
		} catch (UserNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
		try {
			service.delete(id);
			attributes.addFlashAttribute("message", "user deleted successfully!!");
		} catch (UserNotFoundException e) {
			attributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String editUser(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
			RedirectAttributes attributes) {
		service.updateStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user id=" + id + " has been " + status;
		attributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportCsv(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listUsers();
		UserCsvExpoter exporter=new UserCsvExpoter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listUsers();
		UserExcelExpoter exporter=new UserExcelExpoter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listUsers();
		UserPDFExpoter exporter=new UserPDFExpoter();
		exporter.export(listUsers, response);
	}
}
