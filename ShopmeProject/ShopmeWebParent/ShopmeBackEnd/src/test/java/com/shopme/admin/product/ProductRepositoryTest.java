package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Brand;
import com.shopme.common.entities.Category;
import com.shopme.common.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager testManager;
	
	
	@Test
	public void createFirstProductTest() {
		Brand brand=testManager.find(Brand.class, 37L);
		Category category=testManager.find(Category.class, 5L);
		Product product=new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("a good laptop from acer");
		product.setFullDescription("a very good desktop from acer in chipest price in india full description");
		product.setPrice(40000f);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		product.setCost(5000);
		product.setEnabled(true);
		product.setInStock(true);
		product.setBrand(brand);
		product.setCategory(category);
		Product save = repo.save(product);
		assertThat(save).isNotNull();
		assertThat(save.getId()).isGreaterThan(0);
	}
	
	@Test
	public void getAllProductTest() {
		Iterable<Product> findAll = repo.findAll();
		findAll.forEach(System.out::println);
	}
	
	@Test
	public void fingByIdProductTest() {
		Long id=1L;
		Product product = repo.findById(id).get();
		assertThat(product).isNotNull();
		assertThat(product.getId()).isEqualTo(id);
	}
	
	@Test
	public void updateProductTest() {
		Long id=2L;
		Product product = repo.findById(id).get();
		product.setCost(10000f);
		product.setEnabled(true);
		product.setInStock(true);
		Product save = repo.save(product);
		System.out.println(save);
	}
}
