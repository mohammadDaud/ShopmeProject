package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entities.Role;
import com.shopme.common.entities.User;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 4;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	public List<User> listUsers() {
		return (List<User>) userRepository.findAll();
	}

	public Page<User> listByPageUsers(int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE);
		return userRepository.findAll(pageable);
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public User saveUser(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}

	public User updateCurrentUser(User userInForm) {
		User userInDB = userRepository.findById(userInForm.getId()).get();
		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		if (userInForm.getPhoto() != null) {
			userInDB.setPhoto(userInForm.getPhoto());
		}
		userInDB.setFirstname(userInForm.getFirstname());
		userInDB.setLastname(userInForm.getLastname());
		return userRepository.save(userInDB);
	}

	public boolean isEmailUnique(Long id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		if (userByEmail == null)
			return true;
		boolean isCreatedNewUser = (id == null);
		if (isCreatedNewUser) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public User getById(Long id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user!!");
		}
	}

	public void delete(Long id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user!!");
		}
		userRepository.deleteById(id);
	}

	public void updateStatus(Long id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}

	private void encodePassword(User user) {
		String encode = passwordEncoder.encode(user.getPassword());
		user.setPassword(encode);
	}

}
