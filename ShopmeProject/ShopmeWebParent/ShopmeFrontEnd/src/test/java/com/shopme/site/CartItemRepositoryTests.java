package com.shopme.site;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.CartItem;
import com.shopme.common.entities.Customer;
import com.shopme.common.entities.Product;
import com.shopme.site.shoppingcart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void saveTest() {
		Long customerId = 1L;
		Long productId = 1L;
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		CartItem cartItem = new CartItem();
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		CartItem save = cartItemRepository.save(cartItem);
		assertThat(save.getId()).isGreaterThan(0);
	}
}
