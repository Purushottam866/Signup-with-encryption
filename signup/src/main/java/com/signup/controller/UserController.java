package com.signup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signup.dto.UserDto;
import com.signup.service.UserService;

@RestController
public class UserController 
{
	@Autowired
	UserService userService;
	
	@PostMapping("/signup")
	public String signup(@RequestBody UserDto userDto)
	{
		return userService.userSignup(userDto);

	}
	
	@GetMapping("/verify")
	public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
	    return userService.verifyEmail(token);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody UserDto userDto)
	{
		return userService.login();
	}
}
