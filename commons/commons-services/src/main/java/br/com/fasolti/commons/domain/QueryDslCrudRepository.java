package br.com.fasolti.commons.domain;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface QueryDslCrudRepository<E extends DomainObject> extends CrudRepository<E, Long>, QuerydslPredicateExecutor<E> {
}

