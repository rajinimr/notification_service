package com.screenmagic.notificationservice.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.SendFailedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.screen.magic.notificationservice.utils.PrerequisiteException;
import com.screenmagic.notificationservice.NotificationserviceApplication;
import com.screenmagic.notificationservice.model.Mail;
import com.screenmagic.notificationservice.service.Emailer;

@RestController
public class NotificationController {
	
	@GetMapping("/notification_service/v1/connect")
	public ResponseEntity<Object> connect() {
		NotificationserviceApplication.logger.info("/notification_service/v1/connect: The Connection to the Notification Service is Successful - Version 1.0.0 ");
		return new ResponseEntity<>("The Connection to the Notification Service is Successful - Version 1.0.0",
				HttpStatus.OK);
	}

	@PostMapping("/notification_service/v1/sendmail")
	public ResponseEntity<Object> sendMail(@RequestBody Map<String, String> requestBody){
		Map<String,String> responseJson = new HashMap<String, String>();
		HttpStatus httpStatusCode; 
		try {
			Mail mail = new Mail();
			validateEmailAddresses(nullAndEmptyCheckWithException("toRecipients",requestBody.get("toRecipients")).toString());
			mail.setToRecipients(requestBody.get("toRecipients"));
			mail.setCcRecipients(nullCheckWithException("ccRecipients",requestBody.get("ccRecipients")).toString());
			mail.setBccRecipients(nullCheckWithException("bccRecipients",requestBody.get("bccRecipients")).toString());
			mail.setSubject(nullAndEmptyCheckWithException("subject",requestBody.get("subject")).toString());
			mail.setEmailBody(nullAndEmptyCheckWithException("body",requestBody.get("body")).toString());
			
			new Emailer().sendEmail(mail);
			
			responseJson.put("message", "Email sent successfully to "+requestBody.get("toRecipients"));
			httpStatusCode = HttpStatus.OK;
			NotificationserviceApplication.logger.debug("/notification_service/v1/sendmail: RequestBody:  "+requestBody + " Response: "+ responseJson);		
			
		} catch (PrerequisiteException e) {
			responseJson.put("message", e.getMessage());
			httpStatusCode = HttpStatus.PRECONDITION_FAILED;
			NotificationserviceApplication.logger.error("/notification_service/v1/sendmail:"+ e.getMessage());
			
		} catch (SendFailedException e) {
			responseJson.put("message", "One or more invalid Email addresses found ");
			httpStatusCode = HttpStatus.BAD_REQUEST;
			NotificationserviceApplication.logger.error("/notification_service/v1/sendmail:  RequestBody:  "+requestBody + " Response: One or more invalid Email addresses found ");
			
		} catch (Exception e) {
			responseJson.put("message","Exception occured in Notification Service -->" +e.getMessage());
			httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
			NotificationserviceApplication.logger.error("/notification_service/v1/sendmail:  RequestBody:  "+requestBody + " Response: "+ e.getMessage());
			
		}
			
		return new ResponseEntity<Object>(responseJson ,httpStatusCode);
	}
	
	
	
	public Object validateEmailAddresses(String emailId) throws SendFailedException {
		String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
		
		List<String> invalidMailIds = Arrays.stream(emailId.split(","))
											.filter(e -> e.matches(regex) == false)
												.collect(Collectors.toList());
	   
		if(invalidMailIds.size() != 0) {
        	throw new SendFailedException("One or more invalid Email addresses found in the request payload "+invalidMailIds.toString());
        }
        else
        	return emailId;  
	}
	
	private Object nullAndEmptyCheckWithException(String key, Object object) throws PrerequisiteException {
        if(object == null || object.toString().trim().isEmpty()) {
        	throw new PrerequisiteException(key+" is mandatory or cannot be empty");
        }
        else return object;        
	}

	private Object nullCheckWithException(String key, Object object) throws PrerequisiteException {
        if(object == null) {
        	throw new PrerequisiteException(key+" cannot be null. In case of null, please pass empty string i.e., \"\"");
        }
        else return object;        
	}
	
}

