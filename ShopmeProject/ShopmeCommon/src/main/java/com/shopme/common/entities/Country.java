package com.shopme.common.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 45)
	private String name;
	@Column(nullable = false, length = 5)
	private String code;
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	public Country() {
	}

	public Country(Long id) {
		this.id = id;
	}

	public Country(String name) {
		this.name = name;
	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Country(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public Country(String name, String code, Set<State> states) {
		this.name = name;
		this.code = code;
		this.states = states;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", code=" + code + "]";
	}

}
