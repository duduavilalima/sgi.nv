package br.com.fasolti.sgi.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseQueryDslRepository<T, ID extends Serializable> extends CrudRepository<T, ID>, QuerydslPredicateExecutor<T> {
}
