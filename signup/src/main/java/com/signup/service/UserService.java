package com.signup.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.signup.dao.UserDao;
import com.signup.dto.UserDto;
import com.signup.helper.GenerateUserId;
import com.signup.helper.SecurePassword;
import com.signup.helper.SendMail;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	SecurePassword securePassword;
	
	@Autowired
	SendMail sendMail;
	
	@Autowired
	UserDto userDto;
	
	@Autowired
	GenerateUserId generateUserId;
	
    @Value("${jwt.secret}")
    private String secretKey;
	
	Map<String, Object> response = new HashMap<>();
	
	 public ResponseEntity<String> userSignup(UserDto userDto) {
	        List<UserDto> exuser = userDao.findByEmailOrMobile(userDto.getEmail(), userDto.getMobile());
	        if (!exuser.isEmpty()) {
	            return ResponseEntity.badRequest().body("Account already exists");
	        } else {
	            String userId = generateUserId.generateuserId();
	            userDto.setUserId(userId);
	            userDto.setPassword(securePassword.encrypt(userDto.getPassword(), "123"));

	            // Generate verification token
	            String verificationToken = UUID.randomUUID().toString();
	            userDto.setVerificationToken(verificationToken);

	            // Insert user data into the database
	            userDao.insert(userDto);

	            // Send verification email
	            sendMail.sendVerificationEmail(userDto);

	            // Generate JWT for the user
	            String jwt = generateJWT(userDto);
	            System.out.println("Signup success, JWT token : "+jwt);

	            return ResponseEntity.ok("Signup success Check your email for verification ,yor token is token = "+jwt);
	        }
	    }

//	 public ResponseEntity<String> verifyEmail(String token) {
//		    UserDto user = userDao.findByVerificationToken(token);
//		    if (user != null) {
//		        user.setVerify(true);
//		        userDao.update(user);
//		        return ResponseEntity.ok("Email verification successful! Redirecting to login page... Verification token: " + token);
//		    } else {
//		        return ResponseEntity.badRequest().body("Email verification failed! Invalid or expired token.");
//		    }
//		}

	 public ResponseEntity<Object> verifyEmail(String token) {
	        UserDto user = userDao.findByVerificationToken(token);
	        if (user != null) {
	            user.setVerify(true);
	            userDao.update(user);
	            // Constructing JSON response
	            String jsonResponse = "{\"message\": \"Email verification successful! Redirecting to login page...\", \"verificationToken\": \"" + token + "\"}";
	            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
	        } else {
	            // Constructing JSON response for bad request
	            String jsonResponse = "{\"message\": \"Email verification failed! Invalid or expired token.\"}";
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
	        }
	    }
	 
	    public ResponseEntity<String> login(String emph, String password) {
	        long mobile = 0;
	        String email = null;
	        try {
	            mobile = Long.parseLong(emph);
	        } catch (NumberFormatException e) {
	            email = emph;
	        }
	        List<UserDto> users = userDao.findByEmailOrMobile(email, mobile);
	        if (users.isEmpty()) {
	            return ResponseEntity.badRequest().body("Invalid email or mobile");
	        } else {
	            UserDto user = users.get(0);
	            if (securePassword.decrypt(user.getPassword(), "123").equals(password)) {
	                if (user.isVerify()) {
	                    // Generate JWT for the user
	                    String jwt = generateJWT(user);
	                    System.out.println("Login success, JWT token : "+jwt);
	                    return ResponseEntity.ok("login success , token = "+jwt);
	                } else {
	                    // Generate verification token
	                    String verificationToken = UUID.randomUUID().toString();
	                    user.setVerificationToken(verificationToken);
	                    userDao.insert(user);
	                    sendMail.sendVerificationEmail(user);
	                    return ResponseEntity.ok("Please verify your email");
	                }
	            } else {
	                return ResponseEntity.badRequest().body("Invalid password");
	            }
	        }
	    }

	    private String generateJWT(UserDto userDto) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("userId", userDto.getUserId());
	        claims.put("email", userDto.getEmail());

	        // Set expiration time to a very large value or never expire
	        // In this example, expiration time is set to January 1, 3000
	        Date expirationDate = new Date(Long.MAX_VALUE);

	        return Jwts.builder()
	                .setClaims(claims)
	                .setExpiration(expirationDate)
	                .signWith(SignatureAlgorithm.HS256  , secretKey)
	                .compact();
	    }
}
