package com.shopme.site.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepositry categoryRepositry;

	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategory = new ArrayList<>();
		List<Category> categoryList = categoryRepositry.findAllEnabled();
		categoryList.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategory.add(category);
			}
		});
		return listNoChildrenCategory;
	}

	public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
		Category category = categoryRepositry.findByAliasEnabled(alias);
		if (category == null) {
			throw new CategoryNotFoundException("could not found any category");
		}
		return category;
	}

	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();
		Category parentCat = child.getParent();
		while (parentCat != null) {
			listParents.add(0, parentCat);
			parentCat = parentCat.getParent();
		}
		listParents.add(child);
		return listParents;
	}
}
