package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manage everything");
		Role saveRole = roleRepository.save(roleAdmin);
		assertThat(saveRole.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateRestRole() {
		Role roleSalesperson = new Role("Salesperson",
				"manage products price,customers,shipping,orders and sales reports");
		Role roleEditors = new Role("Editors", "manage categories,brands,products and menus");
		Role roleShipper = new Role("Shipper", "manage view products,view orders and update orders status");
		Role roleAssistent = new Role("Assistent", "manage questions and reviews");
		roleRepository.saveAll(List.of(roleSalesperson, roleEditors, roleShipper, roleAssistent));

	}

}
