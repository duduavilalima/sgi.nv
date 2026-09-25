package br.com.araujoabreu.sgi.service.impl.api;

import br.com.araujoabreu.sgi.api.EmailAPIJavamail;
import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.queue.email.JobsEmails;
import br.com.fasolti.queue.email.domain.Email;
import br.com.fasolti.queue.email.services.EmailService;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

@Service
@Primary
public class EmailAPIJavamailImpl implements EmailAPIJavamail {
	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ParametroRepository parametroRepository;

	public void sendEmail(String toAddress, String fromAddress, String subject, String msgBody) {
		logger.info(String.format("Enviando email para: %s. Assunto: %s", toAddress, subject));
		Email.Builder email = JobsEmails.emailTo(toAddress).addFrom(fromAddress).withSubject(subject).withHtmlBody(msgBody);
		emailService.send(email.build());
	}

	public void sendEmailHomologador(String subject, String msgBody) throws MessagingException {
		Parametro email = parametroRepository.findById("fornecedor.homologador.email").orElse(null);
		sendEmail(email.getValor(), "suporte_sgi@copony.com.br", subject, msgBody);
	}

	public void sendEmail(String toAddress, String fromAddress, String subject, String msgBody, File... files) {
		logger.info(String.format("Enviando email para: %s. Assunto: %s", toAddress, subject));
		Email email = JobsEmails.emailTo(toAddress)
			.addFrom(fromAddress)
			.withSubject(subject)
			.addAttachmentFiles(files)
			.htmlBody(msgBody);
		emailService.send(email);
	}

	@Override
	public void sendEmailError(String subject, Throwable error) {
		// FIXME: Colocar em parâmetro de banco de dados.
		String[] emails = new String[]{"marcel.waldeck@fasolti.com.br", "daniel.oliveira@fasolti.com.br"};
		for (String email : emails) {
			logger.info(String.format("Received <%s>", email));
			sendEmail(email, subject, subject, getStackTrace(error));
		}
	}

	@Override
	public void sendEmailError(String[] emails, String subject, String title, Throwable error) {
		for (String email : emails) {
			logger.info(String.format("Received <%s>", email));
			sendEmail(email, subject, subject, getStackTrace(error));
		}
	}

	private String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
