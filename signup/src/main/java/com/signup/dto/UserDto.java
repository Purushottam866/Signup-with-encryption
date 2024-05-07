package com.signup.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Component
@Data
@Entity
public class UserDto 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // Auto-generated ID field by database
	  
	@Column(unique = true) 
	private String userId;
	private String name;
	private String lname;
	private String email;
	private long mobile;
	private String password;
	private String companyname;
	 
	private String verificationToken;
	boolean verify;
	
	 
}
