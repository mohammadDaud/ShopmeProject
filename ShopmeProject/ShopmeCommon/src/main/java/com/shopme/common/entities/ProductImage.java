package com.shopme.common.entities;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage() { }

	public ProductImage(Long id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Transient
	public String getImagePath() {
		return "/product-images/"+product.getId()+"/extras/"+this.name;
	}

}
