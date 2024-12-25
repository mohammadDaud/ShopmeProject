package com.shopme.site.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entities.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.site.MailUtility;
import com.shopme.site.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@PostMapping("cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId") Long productId,
			@PathVariable(name = "quantity") int quantity, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			Long updatedQuantity = cartService.addProduct(quantity, productId, authenticatedCustomer);
			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart.";
		} catch (ShoppingCartException s) {
			return s.getMessage();
		}
	}

	@PostMapping("cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name = "productId") Long productId,
			@PathVariable(name = "quantity") int quantity, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			float updatedQuantity = cartService.updateQuantity(quantity, productId, authenticatedCustomer);
			return String.valueOf(updatedQuantity);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product.";
		}
	}

	@DeleteMapping("cart/remove/{productId}")
	public String removeProduct(@PathVariable(name = "productId") Long productId, HttpServletRequest request) {
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, authenticatedCustomer);
			return "The product has been remove from your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product from cart.";
		}
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = MailUtility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
}
