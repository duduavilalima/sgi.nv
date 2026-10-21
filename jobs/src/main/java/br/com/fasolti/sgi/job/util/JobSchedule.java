package br.com.fasolti.sgi.job.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Agenda a execução de um {@link SgiJob}. A expressão <code>crontab</code> usa a sintaxe do Quartz (como nos jobs
 * originais) e aceita placeholders <code>${...}</code> e expressões SpEL <code>#{...}</code>.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JobSchedule {

	String crontab();

	boolean autostart() default true;
}
