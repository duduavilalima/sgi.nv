package br.com.fasolti.commons.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender mailSender;
	private final EmailTemplateProcessor templateProcessor;
	private final String defaultFrom;
	private final String redirectTo;

	/**
	 * @param redirectTo quando preenchido, todo e-mail é enviado apenas para este endereço (uso em testes);
	 *                   os destinatários originais são registrados no assunto.
	 */
	public EmailService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor, String defaultFrom,
		String redirectTo) {
		this.mailSender = mailSender;
		this.templateProcessor = templateProcessor;
		this.defaultFrom = defaultFrom;
		this.redirectTo = redirectTo == null || redirectTo.trim().isEmpty() ? null : redirectTo.trim();
	}

	public void send(Email email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

			if (email.getFrom().isEmpty()) {
				helper.setFrom(defaultFrom);
			} else {
				for (InternetAddress from : email.getFrom()) {
					helper.setFrom(from);
				}
			}
			if (redirectTo == null) {
				helper.setTo(email.getTo().toArray(new InternetAddress[0]));
				helper.setSubject(email.getSubject());
			} else {
				logger.warn("E-mail redirecionado para {} (destinatários originais: {})", redirectTo, email.getTo());
				helper.setTo(redirectTo);
				helper.setSubject(email.getSubject() + " [originalmente para: " + email.getTo() + "]");
			}
			helper.setText(body(email), true);
			for (File file : email.getAttachments()) {
				helper.addAttachment(file.getName(), file);
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new IllegalStateException("Falha ao montar o e-mail para " + email.getTo(), e);
		}
	}

	private String body(Email email) {
		if (email.getTemplateName() != null) {
			return templateProcessor.process(email.getTemplateName(), email.getTemplateModel()).getContent();
		}
		return email.getHtmlBody();
	}
}
