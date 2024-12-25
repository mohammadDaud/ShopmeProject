package com.shopme.site.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entities.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.site.MailUtility;
import com.shopme.site.setting.EmailSettingBag;
import com.shopme.site.setting.SettingService;

@Controller
public class ForgotPasswordController {

	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String viewRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request,Model model) {
		String email= request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = MailUtility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(email,link);
			model.addAttribute("message", "we have sent a reset password link to your email."
					+ "Please check.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException |MessagingException e) {
			model.addAttribute("error", "could not send email");
		} 
		return "customer/forgot_password_form";
	}
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param("token") String token,Model model) {
		Customer customer = customerService.getCustomerByResetPasswordToken(token);
		if(customer != null) {
			model.addAttribute("token", token);
		}
		else {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetPasswordForm(HttpServletRequest request,Model model) {
		String token= request.getParameter("token");
		String password=request.getParameter("password");
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle", "Reset Password");
			model.addAttribute("title", "Reset Your Password");
			model.addAttribute("message", "you have successfully change your password.");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
			return "message";
		}
	}
	
	private void sendEmail(String email,String link) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl sender = MailUtility.prepareMailSender(emailSettings);
		String toAddress = email;
		String subject = "Here's the link to reset your password";
		String content = "<p>Hello,</p>"
				+ "<p>you have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password</p>"
				+ "<p><a href=\""+link+"\">Change my Password</a></p>"
				+ "<br>"
				+ "<p>Ignore this mail if you do not remember your password ,"
				+ "or you have not made the request.<p>";
				
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		sender.send(message);
	}
}
