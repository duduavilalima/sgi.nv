package br.com.fasolti.commons.services;

import br.com.fasolti.commons.client.service.SearchService;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.util.BeanPathUtils;
import br.com.fasolti.commons.util.DslHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseSearchService<E extends DomainObject, T extends TransferObject, S extends SearchObject, R extends QueryDslCrudRepository<E>>
	extends BaseCrudService<E, T, R>
	implements SearchService<T, S> {

	private final Class<S> searchTransferType;

	@Autowired
	private BeanPathUtils beanPathUtils;

	@Autowired
	private Mapper dozerMapper;

	@Autowired
	protected DslHelper dslHelper;

	@SuppressWarnings("unchecked")
	public BaseSearchService(R repository) {
		super(repository);

		Type type = this.getClass().getGenericSuperclass();

		if (!(type instanceof ParameterizedType)) {
			type = this.getClass().getSuperclass().getGenericSuperclass();
		}

		ParameterizedType genericType = ParameterizedType.class.cast(type);

		this.searchTransferType = (Class<S>) genericType.getActualTypeArguments()[2];
	}

	/**
	 * Extender se necessário usar joins ou subqueries
	 */
	protected void buildSearchQuery(JPAQuery<E> query) {
	}

	/**
	 * Variante de {@link #buildSearchQuery(JPAQuery)} com acesso ao objeto de pesquisa.
	 */
	protected void buildSearchQuery(JPAQuery<E> query, S exampleObj) {
	}

	/**
	 * Extender se necessário criar um predicado mais complexo que apenas ANDs entre as propriedades
	 */
	protected void buildSearchPredicate(BooleanBuilder builder, S exampleObj) {
//FIXME Não está funcionando como deveria
//		E entity = mapSearch(exampleObj);
//		Predicate predicate = beanPathUtils.createSearchPredicate(entityPath, entity);
//		builder.and(predicate);
	}

	protected OrderSpecifier defineSearchOrder(S exampleObj) {
		return null;
	}

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeSearch(S exampleObj) throws ServiceException { }

	@SuppressWarnings("UnusedParameters")
	protected void doAfterSearch(List<E> entities, List<S> result) throws ServiceException { }

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Iterable<S> search(S exampleObj) {
		return search(exampleObj, null);
	}

//	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Iterable<S> search(S exampleObj, Pageable page) {
		doBeforeSearch(exampleObj);

		JPAQuery<E> query = select();

		// Permite montar uma query complexa...
		buildSearchQuery(query);
		buildSearchQuery(query, exampleObj);

		// ... e/ou apenas um predicado
		BooleanBuilder predicateBuilder = new BooleanBuilder();
		buildSearchPredicate(predicateBuilder, exampleObj);

		// Aplica o filtro obrigatório
		Predicate predicate = filterPredicate(predicateBuilder.getValue());

		// Finaliza a query
		query.where(predicate);

		if (page != null) {

		}

		// Busca e retorna
		List<E> entities = query.fetch();

		List<S> result = mapSearch(entities);

		doAfterSearch(entities, result);

		return result;
	}

	protected final S mapSearch(E entity) {
		if (entity == null) {
			return null;
		}

		return dozerMapper.map(entity, searchTransferType);
	}

	protected final E mapSearch(S searchObj) {
		if (searchObj == null) {
			return null;
		}

		return dozerMapper.map(searchObj, entityType);
	}

	protected final List<S> mapSearch(Iterable<E> iterable) {
		return dozerHelper.map(iterable, searchTransferType);
	}

}
