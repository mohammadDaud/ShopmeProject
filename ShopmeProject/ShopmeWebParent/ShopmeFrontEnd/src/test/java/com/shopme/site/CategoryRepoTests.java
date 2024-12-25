package com.shopme.site;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entities.Category;
import com.shopme.site.category.CategoryRepositry;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepoTests {
	
	@Autowired 
	private CategoryRepositry categoryRepositry;
	
	@Test
	public void findAllCategory() {
		List<Category> categories = categoryRepositry.findAllEnabled();
		categories.forEach(category ->{
			System.out.println(category.getName()+" ("+category.getEnabled()+")");
		});
	}

}
