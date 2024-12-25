package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entities.Role;
import com.shopme.common.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin=testEntityManager.find(Role.class, 1L);
		User userDaud=new User("daud@gmail.com","daud2020","Mohammad","Daud");
		userDaud.addRole(roleAdmin);
		User saveUser = userRepository.save(userDaud);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userMohd=new User("mohd@gmail.com","mohd2020","Mohd","Ahmad");
		Role roleEditor=new Role(3L);
		Role roleAssistent=new Role(5L);
		userMohd.addRole(roleEditor);
		userMohd.addRole(roleAssistent);
		User saveUser = userRepository.save(userMohd);
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetAllUsers()
	{
		Iterable<User> all = userRepository.findAll();
		all.forEach(a-> System.out.println(a.getId()));
		
	}
	
	@Test
	public void testGetUsersById()
	{
		Optional<User> optional = userRepository.findById(1L);
		System.out.println(optional.get().getFirstname());
		assertThat(optional.get()).isNotNull();
	}
	
	@Test
	public void testUpdateUsersById()
	{
		User user = userRepository.findById(1L).get();
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	@Test
	public void getUsersByEmail()
	{
		String email="daud@gmail.com";
		User user = userRepository.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
}
