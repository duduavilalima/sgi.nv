package br.com.araujoabreu.sgi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
	"br.com.araujoabreu.sgi",
	"br.com.fasolti"
})
@EnableTransactionManagement
public class PersistenceContext {
}
