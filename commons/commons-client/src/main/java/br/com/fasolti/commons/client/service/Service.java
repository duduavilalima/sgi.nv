package br.com.fasolti.commons.client.service;

import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.ServiceException;

import java.util.Collection;
import java.util.List;

public interface Service<T extends TransferObject> {

	@Deprecated
	T getById(Long id) throws ServiceException;

	T load(Long id) throws ServiceException;

	List<T> listAll() throws ServiceException;

	List<T> listAll(String ... sortFields) throws ServiceException;

	List<Long> extractIds(Collection<T> objects);

}
