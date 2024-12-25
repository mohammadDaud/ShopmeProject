package com.shopme.site.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entities.CartItem;
import com.shopme.common.entities.Customer;
import com.shopme.common.entities.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	public List<CartItem> findByCustomer(Customer customer);

	public CartItem findByCustomerAndProduct(Customer customer, Product product);

	@Modifying
	@Query("UPDATE CartItem c SET c.quantity=?1 WHERE c.customer.id=?2 AND c.product.id=?3")
	public void updateQuantity(int quantity, Long customerId, Long productId);

	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id=?1 AND c.product.id=?2")
	public void deleteByCustomerAndProduct(Long customerId, Long productId);
}
