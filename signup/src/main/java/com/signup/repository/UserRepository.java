package com.signup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.signup.dto.UserDto;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Integer>
{
	UserDto findByVerificationToken(String token);
	List<UserDto> findByEmailOrMobile(String email, long mobile);
	
}
