package br.com.fasolti.queue.appconfig;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Configuration
@ImportResource({"classpath:spring/applicationContext.xml"})
public class SgiJobsServerConfiguration {

	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws IOException {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:spring/conf/*.properties"));
		return ppc;
	}
}
