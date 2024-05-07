package com.signup.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.signup.dao.UserDao;

import lombok.Data;
@Data
@Service
public class GenerateUserId{
	
	 @Autowired
	    UserDao userDao;

	    public String generateuserId() {
	        String lastUserId = userDao.findLastUserId();
	        
	        String userId;
	        
	        if (lastUserId == null) {
	            userId = "QS202401"; // Start from QS202401 if no users exist
	        } else {
	            // Extract the numeric part of the last userId
	            int lastNumber = Integer.parseInt(lastUserId.substring(6));
	            int newNumber = lastNumber + 1;
	            userId = "QS2024" + String.format("%02d", newNumber);
	        }
	        return userId;
	    }
}
