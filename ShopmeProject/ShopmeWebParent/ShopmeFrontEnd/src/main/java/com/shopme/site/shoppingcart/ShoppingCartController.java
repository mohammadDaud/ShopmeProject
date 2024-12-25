package com.shopme.site.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entities.CartItem;
import com.shopme.common.entities.Customer;
import com.shopme.site.MailUtility;
import com.shopme.site.customer.CustomerService;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> listcartItems = cartService.listCartItems(customer);
		float estimatedTotal = 0.0f;
		for (CartItem item : listcartItems) {
			estimatedTotal += item.getSubtotal();
		}
		model.addAttribute("listcartItems", listcartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		return "cart/shopping_cart";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = MailUtility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
