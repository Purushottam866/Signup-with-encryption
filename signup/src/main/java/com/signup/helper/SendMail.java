package com.signup.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.signup.dto.UserDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMail 
{
	@Autowired
	JavaMailSender mailSender;
	
	 public void sendVerificationEmail(UserDto userDto) {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        String verificationLink = "http://localhost:8080/verify?token=" + userDto.getVerificationToken();
	        try {
	            helper.setFrom("demodem866@gmail.com", "QP_Share");
	            helper.setTo(userDto.getEmail());
	            helper.setSubject("Verify Email");

	            // Read HTML template from file
	            String htmlBody = readHtmlTemplate("verification_email_template.html");

	            // Replace placeholders with dynamic content
	            htmlBody = htmlBody.replace("{{USERNAME}}", userDto.getName());
	            htmlBody = htmlBody.replace("{{VERIFICATION_LINK}}", verificationLink);

	            // Set HTML content as the email body
	            helper.setText(htmlBody, true);
	        } catch (UnsupportedEncodingException | MessagingException e) {
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }
	 private String readHtmlTemplate(String templateName) {
		    try {
		        ClassPathResource resource = new ClassPathResource("templates/" + templateName);
		        InputStream inputStream = resource.getInputStream();
		        byte[] bytes = new byte[inputStream.available()];
		        inputStream.read(bytes);
		        return new String(bytes, StandardCharsets.UTF_8);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return ""; 
		    }
		}
}
