package br.com.fasolti.commons.client.service;

import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import org.springframework.data.domain.Pageable;

public interface SearchService<T extends TransferObject, S extends SearchObject> extends CrudService<T> {

	Iterable<S> search(S example);

	/**
	 * Pesquisa paginada; o resultado é um {@link org.springframework.data.domain.Page} quando a página é informada.
	 */
	Iterable<S> search(S example, Pageable page);
}
