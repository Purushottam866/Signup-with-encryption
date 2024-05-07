package com.signup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signup.dto.UserDto;
import com.signup.helper.JwtUtil;
import com.signup.service.UserService;

@RestController
public class UserController 
{
	@Autowired
	UserService userService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	   @PostMapping("/signup")
	    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
	        return userService.userSignup(userDto);
	    }

	    @GetMapping("/verify")
	    public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token) {
	        return userService.verifyEmail(token);
	    }

	    @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestParam String emph, @RequestParam String password) {
	        return userService.login(emph, password);
	    }
	    
	    @GetMapping("/user-details")
	    public ResponseEntity<String> getUserDetails(@RequestHeader("Authorization") String token) {
	        String jwtToken = token.substring(7); // remove "Bearer " prefix
	        String userId = jwtUtil.extractUserId(jwtToken);
	        String email = jwtUtil.extractEmail(jwtToken);

	        // Now you have the user details, you can use them as needed
	        return ResponseEntity.ok("User ID: " + userId + ", Email: " + email);
	    }
	    
}
