package com.stigentech.spares365.service;

import javax.mail.MessagingException;

public interface MailService {
	
	public void send(String toAdress, String fromAddress, String subject, String content) throws MessagingException;

}
