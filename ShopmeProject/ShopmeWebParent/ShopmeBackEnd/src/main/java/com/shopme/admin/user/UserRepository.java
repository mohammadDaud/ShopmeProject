package com.shopme.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entities.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Long id);
	
	@Query("UPDATE User u SET u.enabled=?2 WHERE u.id=?1")
	@Modifying
	public void updateEnabledStatus(Long id,boolean enabled);
}
