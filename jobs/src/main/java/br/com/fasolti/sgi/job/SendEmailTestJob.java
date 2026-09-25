//FIXME TESTE JOB
//package br.com.fasolti.sgi.job;
//
//import br.com.fasolti.queue.commons.api.model.Job;
//import br.com.fasolti.queue.commons.api.model.annotations.ImplementsJob;
//import br.com.fasolti.queue.commons.api.model.annotations.WithJobModel;
//import br.com.fasolti.queue.commons.api.model.entities.JobExecutionInfo;
//import br.com.fasolti.queue.email.JobsEmails;
//import br.com.fasolti.queue.email.domain.Email;
//import br.com.fasolti.queue.email.services.EmailService;
//import br.com.fasolti.queue.email.services.EmailTemplateProcessor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@ImplementsJob
//@WithJobModel(
//	autostart = true,
//	crontab = "* */2 * ? * *")
//public class SendEmailTestJob implements Job {
//
//	@Autowired
//	private EmailService emailService;
//
//	@Override
//	public void process(JobExecutionInfo execution) throws Exception {
//		Email email = JobsEmails
//			.emailTo("flavio.lisboa@fasolti.com.br", "fernando.nicolau@fasolti.com.br")
//			.withSubject("Teste sginv-jobs/rabbitmq")
//			.htmlBody("<h1>TESTE</h1>");
//		emailService.send(email);
//	}
//}
