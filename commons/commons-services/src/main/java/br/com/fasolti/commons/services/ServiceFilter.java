package br.com.fasolti.commons.services;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.commons.util.BeanPathUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

public class ServiceFilter<E extends DomainObject> {
	private final BeanPathUtils beanPathUtils = new BeanPathUtils();

	public final EntityPathBase<E> entityPath;

	public final NumberPath<Long> entityIdPath;

	public final Predicate mandatoryFilter;

	public static <E extends DomainObject> ServiceFilter<E> create(EntityPathBase<E> entityPath, Predicate mandatoryFilter) {
		return new ServiceFilter<>(entityPath, mandatoryFilter);
	}

	private ServiceFilter(EntityPathBase<E> entityPath, Predicate mandatoryFilter) {
		this.entityPath = entityPath;
		this.entityIdPath = beanPathUtils.createLong(entityPath, "id");
		this.mandatoryFilter = mandatoryFilter;
	}
}
