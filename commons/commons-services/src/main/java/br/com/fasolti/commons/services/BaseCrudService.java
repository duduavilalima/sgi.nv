package br.com.fasolti.commons.services;

import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.AuditedObject;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.commons.lang.CreateObjectException;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.util.Dereferencer;
import br.com.fasolti.commons.util.TransactionalObject;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public abstract class BaseCrudService<E extends DomainObject, T extends TransferObject, R extends QueryDslCrudRepository<E>>
	extends BaseService<E, T, R>
	implements CrudService<T> {

	@Autowired
	private SecurityService<?> securityService;

	@Autowired
	private Dereferencer dereferencer;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private Mapper dozerMapper;

	public BaseCrudService(R repository) {
		super(repository);
	}

	protected T createObjectInstance() throws CreateObjectException {
		try {
			return transferType.newInstance();
		} catch (Exception e) {
			throw new CreateObjectException(e);
		}
	}

	protected T doAfterCreate(T object) throws ServiceException {
		return object;
	}

	protected void setDefaultValues(T object) throws ServiceException {
	}

	@Override
	public T create() throws ServiceException {
		T object = createObjectInstance();
		setDefaultValues(object);
		return doAfterCreate(object);
	}

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeSave(T object) throws ServiceException {
	}

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeSave(E entity, T object) throws ServiceException { }

	@Deprecated
	@SuppressWarnings("UnusedParameters")
	protected void doAfterSave(E entity) throws ServiceException { }

	@SuppressWarnings("UnusedParameters")
	protected void doAfterSave(E entity, T object) throws ServiceException {
		doAfterSave(entity);
	}

	@Override
	@Transactional
	public T save(T object) throws ServiceException {
		return map(internalSave(object));
	}

	@Override
	@Transactional
	public List<T> save(Collection<T> list) throws ServiceException {
		List<E> entityList = new ArrayList<>(list.size());

		for (T object : list) {
			E entity = internalSave(object);
			entityList.add(entity);
		}

		return map(entityList);
	}

	private E internalSave(T object) throws ServiceException {
		setDefaultValues(object);

		doBeforeSave(object);

		validator.validate(object);

		E entity = map(object);

		doBeforeSave(entity, object);

		entity.setId(null); // Garante que não veio nenhum lixo

		checkAuditedObject(entity);

		entity = repository.save(entity);

		object.setId(entity.getId());

		doAfterSave(entity, object);

		return entity;
	}

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeUpdate(T object) throws ServiceException { }

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeUpdate(E entity, T object) throws ServiceException { }

	@Deprecated
	@SuppressWarnings("UnusedParameters")
	protected void doAfterUpdate(E entity) throws ServiceException { }

	@SuppressWarnings("UnusedParameters")
	protected void doAfterUpdate(E entity, T object) throws ServiceException {
		doAfterUpdate(entity);
	}

	@Override
	@Transactional
	public T update(T object, Long id) throws ServiceException {
		return map(internalUpdate(object, id));
	}

	@Override
	@Transactional
	public List<T> update(Collection<T> list) throws ServiceException {
		List<E> entityList = new ArrayList<>(list.size());

		for (T object : list) {
			E entity = internalUpdate(object, object.getId());
			entityList.add(entity);
		}

		return map(entityList);
	}

	private E internalUpdate(T object, Long id) throws ServiceException {
		doBeforeUpdate(object);

		validator.validate(object);

		E entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new RuntimeException("Entidade com ID " + id + " não encontrada");
		}

		if (object == null) {
			logger.warn("Objeto nulo na atualização");
			return entity;
		}

		dereferencer.process(entity);

		cleanAuditedObject(object, entity);

		doBeforeUpdate(entity, object);

		dozerMapper.map(object, entity);

		checkAuditedObject(entity);

		entity = repository.save(entity);

		doAfterUpdate(entity, object);

		return entity;
	}

	@Transactional
	public T saveOrUpdate(T object) throws ServiceException {
		if (object.getId() == null)
			return save(object);
		else
			return update(object, object.getId());
	}

	@SuppressWarnings("UnusedParameters")
	protected void doBeforeDelete(T object) throws ServiceException { }

	@Override
	@Transactional
	public void delete(T object) throws ServiceException {
		internalDelete(object);
	}

	@Override
	@Transactional
	public void delete(Collection<T> list) throws ServiceException {
		for (T object : list) {
			internalDelete(object);
		}
	}

	private void internalDelete(T object) {
		doBeforeDelete(object);
		E entity = map(object);
		repository.delete(entity);
	}

	protected void doBeforeTransition(T object, Enum status) throws ServiceException { }

	protected void doAfterTransition(T object, Enum status) throws ServiceException { }

	@Override
	@Transactional
	public T transition(T object, Enum status) throws ServiceException {
		if (!(object instanceof TransactionalObject)) {
			throw new ServiceException("Objeto não pode ser transicionado"); // TODO Usar exceção específica
		}

		doBeforeTransition(object, status);

		TransactionalObject<Enum<?>> transObj = (TransactionalObject) object;
		transObj.setStatus(status);

		object = update(object, object.getId());

		doAfterTransition(object, status);

		return object;
	}

	private void cleanAuditedObject(T object, E entity) {
		// Os dados de auditoria quem vêm nos objetos devem ser ignorados
		if (object instanceof AuditedObject && entity instanceof AuditedObject) {
			AuditedObject auditedObject = (AuditedObject) object;
			AuditedObject auditedEntity = (AuditedObject) entity;
			auditedObject.setCreatedBy(auditedEntity.getCreatedBy());
			auditedObject.setCreationDate(auditedEntity.getCreationDate());
			auditedObject.setModifiedBy(auditedEntity.getModifiedBy());
			auditedObject.setModificationDate(auditedEntity.getModificationDate());
		}
	}

	private void checkAuditedObject(E entity) {
		if (entity instanceof AuditedObject) {
			AuditedObject auditedObject = (AuditedObject) entity;

			Date data = new Date();

			if (entity.getId() == null) {
				auditedObject.setCreationDate(data);
				auditedObject.setCreatedBy(securityService.getUser().getUsername());
			}

			auditedObject.setModificationDate(data);
			auditedObject.setModifiedBy(securityService.getUser().getUsername());
		}
	}

}
