package br.com.fasolti.commons.services;

import br.com.fasolti.commons.GenericsInfo;
import br.com.fasolti.commons.GenericsUtils;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.client.service.Service;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.client.validation.ObjectValidator;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.commons.lang.NotFoundException;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.util.DefaultObjectEntityClassResolver;
import br.com.fasolti.commons.util.DefaultSimpleObjectClassResolver;
import br.com.fasolti.commons.util.DozerHelper;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public class BaseService<E extends DomainObject, T extends TransferObject, R extends QueryDslCrudRepository<E>> implements Service<T> {
	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private EntityManager entityManager;

	@Deprecated
	@Autowired
	/**
	 * Usar a variável "mapper", para desassociar da implementação
	 */
	protected DozerHelper dozerHelper;

	@Autowired
	protected DozerHelper mapper;

	//TODO Permitir ser escolhido pela aplicação
	@Autowired
	private DefaultObjectEntityClassResolver objectEntityResolver;

	//TODO Permitir ser escolhido pela aplicação
	@Autowired
	private DefaultSimpleObjectClassResolver simpleObjectResolver;

	@Autowired
	private Mapper dozerMapper;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Autowired(required = false)
	private SecurityService<? extends UserDetails> baseSecurityService;

	protected final ObjectValidator<T> validator;

	protected final Class<E> entityType;

	protected final Class<T> transferType;

	protected final PathBuilder<E> pathBuilder;

	protected final EntityPath<E> entityPath;

	protected final R repository;

	public BaseService(R repository) {
		this.repository = repository;

		GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);

		this.entityType = genericsInfo.getType(0);
		this.transferType = genericsInfo.getType(1);

		this.pathBuilder = new PathBuilder<>(entityType, Util.varName(entityType));
		this.entityPath = this.pathBuilder;

		this.validator = new ObjectValidator<>();
	}

	protected final EntityManager getEntityManager() {
		return entityManager;
	}

	protected final R getRepository() {
		return repository;
	}

	protected final DozerHelper getMapper() {
		return mapper;
	}

	protected final JPAQueryFactory getJPAQueryFactory() {
		return jpaQueryFactory;
	}

	/**
	 * Usuário autenticado no contexto de segurança atual.
	 */
	protected final UserDetails getUser() {
		return baseSecurityService.getUser();
	}

	protected ServiceFilter<E> getServiceFilter() {
		return null;
	}

	protected final <U> JPAQuery<U> filterQuery(JPAQuery<U> query) {
		if (getServiceFilter() != null) {
			query.where(getServiceFilter().mandatoryFilter);
		}
		return query;
	}

	protected final Predicate filterPredicate(Predicate predicate) {
		if (getServiceFilter() != null) {
			predicate = new BooleanBuilder(predicate).and(getServiceFilter().mandatoryFilter);
		}
		return predicate;
	}

	protected OrderSpecifier<?>[] defaultOrder() {
		return null;
	}

	/**
	 * Extender para customizar a query usada em {@link #load(Long)} (ex.: projeções). Quando sobrescrito, o
	 * carregamento deixa de usar o repositório e passa a executar a query montada aqui.
	 */
	protected void buildLoadQuery(JPAQuery<E> query) {
	}

	private boolean overridesLoadQuery() {
		try {
			return getClass().getDeclaredMethod("buildLoadQuery", JPAQuery.class).getDeclaringClass() != BaseService.class;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private E loadEntity(Long id) {
		if (overridesLoadQuery()) {
			String idName = entityManager.getMetamodel().entity(entityType).getId(Long.class).getName();
			JPAQuery<E> query = select().where(pathBuilder.getNumber(idName, Long.class).eq(id));
			buildLoadQuery(query);
			return query.fetchOne();
		}
		if (getServiceFilter() == null) {
			return repository.findById(id).orElse(null);
		}
		return repository.findOne(filterPredicate(getServiceFilter().entityIdPath.eq(id))).orElse(null);
	}

	@SuppressWarnings("UnusedParameters")
	protected void doAfterLoad(T object) throws ServiceException { }

	@Override
	@Deprecated
	public T getById(Long id) throws ServiceException {
		return load(id);
	}

	@Override
	public T load(Long id) throws ServiceException {
		E entity = loadEntity(id);

		if (entity == null) {
			throw new NotFoundException(entityType, id);
		}

		T transferObj = map(entity);
		doAfterLoad(transferObj);

		return transferObj;
	}

	@Override
	public List<T> listAll() throws ServiceException {
		return list(null, defaultOrder());
	}

	@Override
	public List<T> listAll(String ... sortFields) {
		return list(null, createOrderSpecifier(sortFields));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> extractIds(Collection<T> objects) {
		return Lists.newArrayList(CollectionUtils.collect(objects, new Transformer() {
			@Override
			public Object transform(Object input) {
				return ((TransferObject) input).getId();
			}
		}));
	}

	protected final List<T> list(Predicate predicate) throws ServiceException {
		return list(predicate, defaultOrder());
	}

	protected final List<T> list(Predicate predicate, OrderSpecifier<?> ... orderSpecifiers) throws ServiceException {
		predicate = filterPredicate(predicate);

		Iterable<E> entityList;
		if (orderSpecifiers != null)
			entityList = repository.findAll(predicate, orderSpecifiers);
		else
			entityList = repository.findAll(predicate);

		List<T> objectList = map(entityList);

		for (T obj : objectList) {
			doAfterLoad(obj);
		}

		return objectList;
	}

	protected final OrderSpecifier<?>[] createOrderSpecifier(String... sortFields) {
		if (sortFields == null || sortFields.length == 0)
			return null;

		List<OrderSpecifier<?>> orders = new ArrayList<>(sortFields.length);
		for (String field : sortFields) {
			Order direction = Order.ASC;
			if (field.matches("[+-].*")) {
				if (field.charAt(0) == '-')
					direction = Order.DESC;
				field = field.substring(1);
			}
			orders.add(new OrderSpecifier<>(direction, pathBuilder.get(field, Comparable.class)));
		}

		return orders.toArray(new OrderSpecifier[sortFields.length]);
	}

//TODO
//	public List<T> list(Long ... ids) {
//		select().where();
//		BooleanBuilder builder = new BooleanBuilder();
//
//	}

	protected final Long count(Predicate predicate) {
		return repository.count(filterPredicate(predicate));
	}

	protected final JPAQuery<E> select() {
		return select(defaultOrder());
	}

	protected final JPAQuery<E> select(OrderSpecifier<?> ... orderSpecifiers) {
		JPAQuery<E> query = jpaQueryFactory.selectFrom(entityPath);

		if (orderSpecifiers != null)
			query.orderBy(orderSpecifiers);

		return filterQuery(query);
	}

	protected final <U> JPAQuery<U> select(Expression<U> projection) {
		return filterQuery(jpaQueryFactory.selectFrom(entityPath).select(projection));
	}

	protected final JPAUpdateClause selectUpdate() {
		return jpaQueryFactory.update(entityPath);
	}

	protected final JPAUpdateClause selectUpdate(Predicate predicate) {
		return selectUpdate().where(filterPredicate(predicate));
	}

	protected final JPADeleteClause selectDelete() {
		return jpaQueryFactory.delete(entityPath);
	}

	protected final JPADeleteClause selectDelete(Predicate predicate) {
		return selectDelete().where(filterPredicate(predicate));
	}

	protected final T map(E entity) {
		if (entity == null) {
			return null;
		}

		return dozerMapper.map(entity, transferType);
	}

	protected final E map(T object) {
		if (object == null) {
			return null;
		}

		return dozerMapper.map(object, entityType);
	}

	protected final List<T> map(Iterable<E> iterable) {
		return dozerHelper.map(iterable, transferType);
	}

	protected final <P extends TransferObject, Q extends DomainObject> P mapDefault(Q mapable) {
		return mapper.map(mapable, objectEntityResolver);
	}

	protected final <P extends TransferObject, Q extends DomainObject> Q mapDefault(P mapable) {
		return mapper.map(mapable, objectEntityResolver);
	}

	protected final <P extends TransferObject, Q extends DomainObject> List<P> mapDefault(Iterable<Q> mapable) {
		return mapper.map(mapable, objectEntityResolver);
	}

	protected final <P extends TransferObject, Q extends TransferObject> Q mapSimple(P object) {
		return mapper.map(object, simpleObjectResolver);
	}

}

