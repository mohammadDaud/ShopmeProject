package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository catRepo;

	@Test
	public void createRootCategoryTest() {
		Category category = new Category("Electronics");
		Category save = catRepo.save(category);
		assertThat(save.getId()).isGreaterThan(0);
	}

	@Test
	public void createSubCategoryTest() {
		Category parent = new Category(6L);
		Category memory = new Category("Sony", parent);
		Category save= catRepo.save(memory);
		assertThat(save.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category parent = catRepo.findById(2L).get();
		System.out.println(parent.getName());
		for(Category category:parent.getChildren()) {
			System.out.println(category.getName());
		}
	}
	
	@Test
	public void testGetCategoryByName() {
		String name="Computers";
		Category parent = catRepo.findByName(name);
		System.out.println(parent.getName());
		assertThat(parent).isNotNull();
		assertThat(parent.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetCategoryByAlias() {
		String name="Computers";
		Category parent = catRepo.findByAlias(name);
		System.out.println(parent.getName());
		assertThat(parent).isNotNull();
		assertThat(parent.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetAllCategory() {
		Iterable<Category> category = catRepo.findAll();
		for(Category cat:category) {
			if(cat.getParent()==null) {
				System.out.println(cat.getName());
				Set<Category> children = cat.getChildren();
				for(Category child:children) {
					System.out.println("--"+child.getName());
					printChildren(child,1L);
				}
			}
		}
	}
	
	private void printChildren(Category parent,long subLevel) {
		long newSubLevel=subLevel+1;
		Set<Category> children = parent.getChildren();
		for(Category subCategory:children) {
			for(long i=0;i<newSubLevel;i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}
	}
}