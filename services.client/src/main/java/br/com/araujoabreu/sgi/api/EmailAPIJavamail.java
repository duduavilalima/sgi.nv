package br.com.araujoabreu.sgi.api;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailAPIJavamail {
	void sendEmail(String toAddress, String fromAddress, String subject, String msgBody);

	void sendEmailHomologador(String subject, String msgBody) throws MessagingException;

	void sendEmail(String toAddress, String fromAddress, String subject, String msgBody, File... files);

	void sendEmailError(String subject, Throwable error);

	void sendEmailError(String[] emails, String subject, String title, Throwable error);
}
