package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entities.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
@Transactional
public class CategoryService {

	public static final int CATEGORY_PER_PAGE = 4;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> listCategories() {
		List<Category> rootCategory = categoryRepository.findRootCategory();
		return listHierarchicalCategories(rootCategory);
	}

	public Page<Category> listByPageCategories(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE);
		return categoryRepository.findAll(pageable);
	}

	public Category saveCategory(Category category) {
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		return categoryRepository.save(category);
	}

	public Category getCategoryById(Long id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find any user!!");
		}
	}

	public void delete(Long id) throws CategoryNotFoundException {
		Long countById = categoryRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any user!!");
		}
		categoryRepository.deleteById(id);
	}

	public void updateStatus(Long id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}

	public boolean isNameUnique(Long id, String name) {
		Category categoryByName = categoryRepository.findByName(name);
		if (categoryByName == null)
			return true;
		boolean isCreatedNewCategory = (id == null);
		if (isCreatedNewCategory) {
			if (categoryByName != null)
				return false;
		} else {
			if (categoryByName.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public List<Category> listCategoriesInForm() {
		List<Category> listCategoryInform = new ArrayList<>();
		Iterable<Category> category = categoryRepository.findAll();
		for (Category cat : category) {
			if (cat.getParent() == null) {
				listCategoryInform.add(Category.category_Copy(cat));
				Set<Category> children = cat.getChildren();
				for (Category child : children) {
					String name = "--" + child.getName();
					listCategoryInform.add(Category.category_Copy(child.getId(), name));
					listSubCategoriesInForm(listCategoryInform, child, 1L);
				}
			}
		}

		return listCategoryInform;
	}

	private void listSubCategoriesInForm(List<Category> listCategoryInform, Category parent, long subLevel) {
		long newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";
			for (long i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			listCategoryInform.add(Category.category_Copy(subCategory.getId(), name));
			listSubCategoriesInForm(listCategoryInform, subCategory, newSubLevel);
		}
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategory) {
		List<Category> hierarchical = new ArrayList<>();
		for (Category root : rootCategory) {
			if (root.getParent() == null) {
				hierarchical.add(Category.category_Copy(root));
				Set<Category> children = root.getChildren();
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					hierarchical.add(Category.category_CopyFull(subCategory, name));
					listSubHierarchicalCategories(hierarchical, subCategory, 1L);
				}
			}
		}
		return hierarchical;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchical, Category parent, long subLevel) {
		long newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";
			for (long i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchical.add(Category.category_CopyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchical, subCategory, newSubLevel);
		}
	}

	public String checkUnique(Long id, String name, String alias) {
		boolean iscreatedNew = (id == null || id == 0);

		Category categoryByName = categoryRepository.findByName(name);
		if (iscreatedNew) {
			if (categoryByName != null) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = categoryRepository.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "OK";
	}
}
