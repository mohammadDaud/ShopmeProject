package com.shopme.site.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.CartItem;
import com.shopme.common.entities.Customer;
import com.shopme.common.entities.Product;
import com.shopme.site.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private ProductRepository productRepository;

	public Long addProduct(int quantity, Long productId, Customer customer) throws ShoppingCartException {
		Long updatedQuantity = (long) quantity;
		Product product = new Product(productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		if (cartItem != null) {
			updatedQuantity = (long) (cartItem.getQuantity() + quantity);
			if (updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more" + quantity + " item(s) because there's already "
						+ cartItem.getQuantity() + " item(s) in your shopping cart.Maximum allowed quantity is 5.");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity.intValue());
		cartItemRepository.save(cartItem);
		return updatedQuantity;
	}

	public List<CartItem> listCartItems(Customer customer) {
		return cartItemRepository.findByCustomer(customer);

	}

	public float updateQuantity(int quantity, Long productId, Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).get();
		float subtotal = product.getDiscountPrice() * quantity;
		return subtotal;
	}

	public void removeProduct(Long productId, Customer customer) {
		cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}
}
