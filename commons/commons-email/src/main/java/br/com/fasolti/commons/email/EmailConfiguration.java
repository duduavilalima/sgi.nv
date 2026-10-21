package br.com.fasolti.commons.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

	@Value("${mail.host:localhost}")
	private String host;

	@Value("${mail.port:25}")
	private int port;

	@Value("${mail.username:}")
	private String username;

	@Value("${mail.password:}")
	private String password;

	@Value("${mail.starttls:false}")
	private boolean starttls;

	@Value("${mail.ssl:false}")
	private boolean ssl;

	@Value("${mail.debug:false}")
	private boolean debug;

	@Value("${mail.redirect-to:}")
	private String redirectTo;

	@Value("${mail.from:suporte_sgi@copony.com.br}")
	private String defaultFrom;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(host);
		sender.setPort(port);
		sender.setDefaultEncoding("UTF-8");

		Properties props = sender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		if (!username.isEmpty()) {
			sender.setUsername(username);
			sender.setPassword(password);
			props.put("mail.smtp.auth", "true");
		}
		props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
		props.put("mail.smtp.ssl.enable", String.valueOf(ssl));
		props.put("mail.debug", String.valueOf(debug));
		return sender;
	}

	@Bean
	public EmailTemplateProcessor emailTemplateProcessor() {
		return new EmailTemplateProcessor();
	}

	@Bean
	public EmailService emailService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor) {
		return new EmailService(mailSender, templateProcessor, defaultFrom, redirectTo);
	}
}
