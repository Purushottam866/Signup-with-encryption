package com.signup.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Component
@Data
@Entity
public class UserDto 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String lname;
	private String email;
	private long mobile;
	private String password;
	private String companyname;
	
	private String verificationToken;
	boolean verify;
	
}
