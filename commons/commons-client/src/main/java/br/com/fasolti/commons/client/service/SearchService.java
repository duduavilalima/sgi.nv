package br.com.fasolti.commons.client.service;

import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;

public interface SearchService<T extends TransferObject, S extends SearchObject> extends CrudService<T> {

	Iterable<S> search(S example);

//FIXME	Iterable<S> search(S example, Pageable page);
}
