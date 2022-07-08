package com.screenmagic.notificationservice.service;


import java.util.Base64;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.screenmagic.notificationservice.NotificationserviceApplication;
import com.screenmagic.notificationservice.model.Mail;



public class Emailer {
	
	public static Properties smtpProps;
	
	public static String decodePassword(String encodedPwd) {
		byte[] decoded = Base64.getDecoder().decode(encodedPwd);
		return new String(decoded);
	}
	
	public void sendEmail(Mail mail) throws Exception {
	    String user = smtpProps.getProperty("mail.username");
	    String password = smtpProps.getProperty("mail.password"); 

		Properties props = new Properties();

		props.put("mail.smtp.auth", smtpProps.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", smtpProps.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", smtpProps.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", smtpProps.getProperty("mail.smtp.port"));
		props.put("mail.smtp.debug", smtpProps.getProperty("mail.smtp.debug"));
		props.put("mail.smtp.ssl.protocols", smtpProps.getProperty("mail.smtp.ssl.protocols"));
		props.put("mail.store.protocol", smtpProps.getProperty("mail.store.protocol"));
		
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

	    
	    Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
	    
	    session.setDebug(true);
	    try {
	      MimeMessage message = new MimeMessage(session);
	      
	      message.setFrom((Address)new InternetAddress(smtpProps.getProperty("mail.smtp.senderId")));
	      InternetAddress[] recipientAddress = new InternetAddress[(mail.getToRecipients().split(",")).length];
	      int counter = 0;
	      for (String toRecipient : mail.getToRecipients().split(",")) {
	        recipientAddress[counter] = new InternetAddress(toRecipient.trim());
	        counter++;
	      } 
	      message.setRecipients(Message.RecipientType.TO, (Address[])recipientAddress);
	      
	      InternetAddress[] ccRecipientAddress = new InternetAddress[(mail.getCcRecipients().split(",")).length];
	      
	      if(!mail.getCcRecipients().equals("")) {
	    	  counter = 0;
		      for (String recipient : mail.getCcRecipients().split(",")) {
		        ccRecipientAddress[counter] = new InternetAddress(recipient.trim());
		        counter++;
		      } 
		      message.setRecipients(Message.RecipientType.CC, (Address[])ccRecipientAddress);  
	      }
	      
	      InternetAddress[] bccRecipientAddress = new InternetAddress[(mail.getBccRecipients().split(",")).length];
	      
	      if(!mail.getBccRecipients().equals("")) {
	    	  counter = 0;
		      for (String recipient : mail.getBccRecipients().split(",")) {
		        bccRecipientAddress[counter] = new InternetAddress(recipient.trim());
		        counter++;
		      } 
		      message.setRecipients(Message.RecipientType.BCC, (Address[])bccRecipientAddress);  
	      }
	      
	      message.setSubject(mail.getSubject());
	      
	      MimeMultipart mimeMultipart = new MimeMultipart();

	      MimeBodyPart mimeBodyPart = new MimeBodyPart();
		  mimeBodyPart.setContent(mail.getEmailBody(), "text/html");

		  mimeMultipart.addBodyPart(mimeBodyPart);
		  message.setContent(mimeMultipart);
	      Transport.send(message);
	      NotificationserviceApplication.logger.info("Successfully sent email.");
	      
	    } catch (Exception e) {
	    	 NotificationserviceApplication.logger.error(e.getMessage());
	         throw new Exception("Exception occured while sending email: "+e.getMessage());
	    } 
	  }
	
}
