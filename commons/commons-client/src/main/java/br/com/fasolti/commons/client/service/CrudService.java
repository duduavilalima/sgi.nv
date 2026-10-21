package br.com.fasolti.commons.client.service;

import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.ServiceException;

import java.util.Collection;
import java.util.List;

public interface CrudService<T extends TransferObject> extends Service<T> {

	T create() throws ServiceException;

	T save(T object) throws ServiceException;

	List<T> save(Collection<T> list) throws ServiceException;

	T update(T object, Long id) throws ServiceException;

	List<T> update(Collection<T> list) throws ServiceException;

	T saveOrUpdate(T object) throws ServiceException;

	void delete(T object) throws ServiceException;

	void delete(Collection<T> list) throws ServiceException;

	T transition(T object, Enum status);

}
