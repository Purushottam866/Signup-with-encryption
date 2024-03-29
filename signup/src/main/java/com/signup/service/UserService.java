package com.signup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.signup.dao.UserDao;
import com.signup.dto.UserDto;
import com.signup.helper.SecurePassword;
import com.signup.helper.SendMail;


@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	SecurePassword securePassword;
	
	@Autowired
	SendMail sendMail;
	
	Map<String, Object> response = new HashMap<>();
	
	public String userSignup(UserDto userDto) {
		List<UserDto> exuser=userDao.findByEmailOrMobile(userDto.getEmail(), userDto.getMobile());
		if(!exuser.isEmpty())
		{
			return "Account already exist";
		}
		else
		{
			//Generating otp
			
			userDto.setPassword(securePassword.encrypt(userDto.getPassword(), "123"));
			userDao.insert(userDto);
			
			//Generate verification link
			  String verificationToken = UUID.randomUUID().toString();
			  userDto.setVerificationToken(verificationToken);
			  userDao.insert(userDto);
			  
			  //send verification link to email
			  sendMail.sendVerificationEmail(userDto);
			
		
			return "Signup Success please check your email! ";
		}
	}

	 public ResponseEntity<String> verifyEmail(String token) {
	        UserDto user = userDao.findByVerificationToken(token);
	        if (user != null) {
	            user.setVerify(true);
	            userDao.update(user);
	            return ResponseEntity.ok("Email verification successful! Redirecting to login page...");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email verification failed! Invalid or expired token.");
	        }
	    }
	
	public String login()
	{
		return null;
	}



	
	
	

}
