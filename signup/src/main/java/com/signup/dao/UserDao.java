package com.signup.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.signup.dto.UserDto;
import com.signup.repository.UserRepository;

@Service
public class UserDao 
{
	@Autowired
	UserRepository userRepository;
	
	public List<UserDto> findByEmailOrMobile(String email,long mobile)
	{
		return userRepository.findByEmailOrMobile(email,mobile);
	}
	
	public void insert(UserDto userDto)
	{
		userRepository.save(userDto);
	}

	public UserDto findById(int id) {
		 return userRepository.findById(id).orElseThrow(null);
		
	}

	public void update(UserDto user) {
		userRepository.save(user);
		
	}

	public UserDto findByVerificationToken(String token) {
		return findByVerificationToken(token);
	}
	
	
}
