package com.shopme.common.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	@Column(length = 64, nullable = false, unique = true)
	private String alias;
	@Column(length = 128, nullable = false)
	private String image;
	@Column(name = "enabled")
	private boolean enabled;
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<Category>();
	@Column(name = "all_parent_ids" ,length = 256)
	private String allParentIDs;
	
	public Category() {
	}

	public Category(Long id) {
		this.id = id;
	}

	public Category(String name, Category parent) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
		this.parent = parent;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}
	
	public Category(Long id, String name, String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	@Transient
	public String getImagesPath() {
		if (id == null || image == null)
			return "/images/category/default_user.png";
		return "/category-images/" + this.id + "/" + this.image;
	}

	public static Category category_Copy(Category cate) {
		Category category = new Category();
		category.setId(cate.getId());
		category.setName(cate.getName());
		return category;
	}

	public static Category category_Copy(Long id, String name) {
		Category category = new Category();
		category.setId(id);
		category.setName(name);
		return category;
	}

	public static Category category_CopyFull(Category cate) {
		Category category = new Category();
		category.setId(cate.getId());
		category.setName(cate.getName());
		category.setAlias(cate.getAlias());
		category.setImage(cate.getImage());
		category.setEnabled(cate.getEnabled());
		return category;
	}

	public static Category category_CopyFull(Category cate, String name) {
		Category category = Category.category_CopyFull(cate);
		category.setName(name);
		return category;
	}
}
