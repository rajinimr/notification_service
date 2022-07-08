package com.screenmagic.notificationservice.model;

public class Mail {
	

	private String toRecipients;
	private String ccRecipients;
	private String bccRecipients;
	private String subject;
	private String emailBody;
	
	public String getToRecipients() {
		return toRecipients;
	}
	public void setToRecipients(String toRecipients) {
		this.toRecipients = toRecipients;
	}
	public String getCcRecipients() {
		return ccRecipients;
	}
	public void setCcRecipients(String ccRecipients) {
		this.ccRecipients = ccRecipients;
	}
	public String getBccRecipients() {
		return bccRecipients;
	}
	public void setBccRecipients(String bccRecipients) {
		this.bccRecipients = bccRecipients;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

}
