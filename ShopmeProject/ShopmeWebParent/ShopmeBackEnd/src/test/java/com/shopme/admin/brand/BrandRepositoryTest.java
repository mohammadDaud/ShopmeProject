package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Brand;
import com.shopme.common.entities.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

	@Autowired
	private BrandRepository repo;

	@Test
	public void testCreateBrand1() {
		Category category = new Category(6L);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(category);
		Brand save = repo.save(acer);
		assertThat(save.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand2() {
		Category cellPhone = new Category(4L);
		Category tablets = new Category(7L);
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellPhone);
		apple.getCategories().add(tablets);
		Brand save = repo.save(apple);
		assertThat(save.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand3() {
		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(new Category(29L));
		samsung.getCategories().add(new Category(24L));
		Brand save = repo.save(samsung);
		assertThat(save.getId()).isGreaterThan(0);
	}

	@Test
	public void testfindAll() {
		Iterable<Brand> findAll = repo.findAll();
		findAll.forEach(System.out::println);
		assertThat(findAll).isNotEmpty();
	}
	
	@Test
	public void testfindById() {
		Brand brand = repo.findById(1L).get();
		assertThat(brand.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName="Samsung Electronics";
		Brand samsung = repo.findById(3L).get();
		samsung.setName(newName);
		Brand save = repo.save(samsung);
		assertThat(save.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDeleteById() {
		Long id=2L;
		repo.deleteById(id);
		Optional<Brand> findById = repo.findById(id);
		assertThat(findById).isEmpty();
	}

}
