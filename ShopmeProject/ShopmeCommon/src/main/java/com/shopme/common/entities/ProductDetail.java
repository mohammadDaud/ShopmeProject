package com.shopme.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_details")
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false,length = 255)
	private String name;
	
	@Column(nullable = false,length = 255)
	private String value;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductDetail() { }

	public ProductDetail(Long id, String name, String value, Product product) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public ProductDetail(String name,String value, Product product) {
		this.name = name;
		this.value = value;
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
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
