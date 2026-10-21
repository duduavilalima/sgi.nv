package br.com.fasolti.sgi.job.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Agenda todos os beans {@link SgiJob} anotados com {@link JobSchedule}. Cada job executa no máximo uma vez por vez:
 * se a execução anterior ainda não terminou, a nova é ignorada.
 */
@Component
public class JobScheduler implements ApplicationListener<ContextRefreshedEvent> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	private final AtomicBoolean started = new AtomicBoolean(false);

	@Value("${jobs.scheduler.enabled:true}")
	private boolean enabled;

	public JobScheduler() {
		scheduler.setPoolSize(4);
		scheduler.setThreadNamePrefix("sgi-job-");
		scheduler.initialize();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() != null || !started.compareAndSet(false, true)) {
			return;
		}

		if (!enabled) {
			logger.warn("Agendamento de jobs desabilitado (jobs.scheduler.enabled=false).");
			return;
		}

		ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory) event.getApplicationContext()
			.getAutowireCapableBeanFactory();
		EmbeddedValueResolver resolver = new EmbeddedValueResolver(beanFactory);

		Map<String, SgiJob> jobs = event.getApplicationContext().getBeansOfType(SgiJob.class);
		jobs.forEach((name, job) -> {
			JobSchedule schedule = AnnotationUtils.findAnnotation(job.getClass(), JobSchedule.class);
			if (schedule == null || !schedule.autostart()) {
				return;
			}
			String crontab = evaluate(beanFactory, resolver, schedule.crontab());
			String cron = QuartzCron.toSpring(crontab);
			logger.info("Agendando job {} com crontab '{}' (Spring: '{}')", job.getClass().getSimpleName(), crontab, cron);
			scheduler.schedule(runnable(job), new CronTrigger(cron));
		});
	}

	private static String evaluate(ConfigurableBeanFactory beanFactory, EmbeddedValueResolver resolver, String value) {
		String resolved = resolver.resolveStringValue(value);
		if (beanFactory.getBeanExpressionResolver() == null) {
			return resolved;
		}
		Object result = beanFactory.getBeanExpressionResolver()
			.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
		return String.valueOf(result);
	}

	private Runnable runnable(SgiJob job) {
		AtomicBoolean running = new AtomicBoolean(false);
		return () -> {
			if (!running.compareAndSet(false, true)) {
				logger.warn("Job {} ainda em execução; ignorando disparo.", job.getClass().getSimpleName());
				return;
			}
			try {
				job.process(new JobExecutionInfo(job.getClass().getSimpleName(), LocalDateTime.now()));
			} catch (Exception e) {
				logger.error("Falha na execução do job {}", job.getClass().getSimpleName(), e);
			} finally {
				running.set(false);
			}
		};
	}

	@PreDestroy
	public void shutdown() {
		scheduler.shutdown();
	}
}
