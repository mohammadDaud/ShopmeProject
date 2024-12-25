package com.shopme.site.customer;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entities.Country;
import com.shopme.common.entities.Customer;
import com.shopme.site.MailUtility;
import com.shopme.site.security.CustomerUserDetails;
import com.shopme.site.security.oauth.CustomerOauth2User;
import com.shopme.site.setting.EmailSettingBag;
import com.shopme.site.setting.SettingService;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SettingService settingsService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());
		return "register/register_form";
	}

	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		model.addAttribute("pageTitle", "Registration Succeeded");
		return "register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingsService.getEmailSettings();
		JavaMailSenderImpl sender = MailUtility.prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", customer.getFullName());

		String verifyURL = MailUtility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		sender.send(message);
	}

	@GetMapping("/verify")
	public String verifAccount(@Param("code") String code, Model model) {
		boolean verify = customerService.verify(code);

		return "register/" + (verify ? "verify_success" : "verify_fail");
	}

	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = MailUtility.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);
		return "customer/account_form";
	}


	@PostMapping("/update_account_details")
	public String updateAccountDetails(Customer customer, Model model, HttpServletRequest request,
			RedirectAttributes attributes) throws UnsupportedEncodingException, MessagingException {
		customerService.updateCustomer(customer);
		updateNameAuthenticatedCustomer(customer, request);
		attributes.addFlashAttribute("message", "Your Account Details Updated Successfully !!!");
		return "redirect:/account_details";
	}

	private void updateNameAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetails(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
			CustomerOauth2User oauth2User = (CustomerOauth2User) oauthToken.getPrincipal();
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			oauth2User.setFullName(fullName);
		}
	}

	private CustomerUserDetails getCustomerUserDetails(Object principal) {
		CustomerUserDetails userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}else if(principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken rtoken=(RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) rtoken.getPrincipal();
		}
		return userDetails;
	}
}
