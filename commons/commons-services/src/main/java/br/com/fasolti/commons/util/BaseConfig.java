package br.com.fasolti.commons.util;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class BaseConfig {
	@PersistenceContext
	private EntityManager em;

	@Bean
	public JPAQueryFactory getJPAQueryFactory() {
		return new JPAQueryFactory(em);
	}

}
