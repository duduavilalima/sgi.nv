package br.com.fasolti.sgi.jobs;

import br.com.fasolti.queue.appconfig.SgiJobsServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Locale;

/**
 * Ponto de entrada do serviço de jobs. A infraestrutura (DataSource, JPA, serviços) vem do contexto XML do SGI
 * importado por {@link SgiJobsServerConfiguration}; o agendamento é feito por
 * {@link br.com.fasolti.sgi.job.util.JobScheduler}.
 */
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import(SgiJobsServerConfiguration.class)
public class JobsServiceApplication {

	public static void main(String[] args) {
		// Formatos de data e textos do SGI assumem o português do Brasil, independentemente do locale do servidor.
		Locale.setDefault(new Locale("pt", "BR"));
		SpringApplication.run(JobsServiceApplication.class, args);
	}
}
