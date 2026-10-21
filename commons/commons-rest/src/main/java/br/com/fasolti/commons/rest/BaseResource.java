package br.com.fasolti.commons.rest;

import br.com.fasolti.commons.client.service.Service;
import br.com.fasolti.commons.client.transfer.TransferObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public abstract class BaseResource<T extends TransferObject> extends ResponseSupport {
	protected static final String ID = "/{id:[0-9]+}";

	protected static final String ALL = "/all";

	protected final Service<T> service;

	public BaseResource(Service<T> service) {
		this.service = service;
	}

	protected void doAfterLoad(T object) {
	}

	protected final T load(Long id) {
		T object = service.getById(id);
		doAfterLoad(object);
		return object;
	}

	@RequestMapping(method = RequestMethod.GET, value = ID)
	public final HttpEntity<T> getById(@PathVariable Long id) {
		return ResponseEntity.ok(load(id));
	}

	@RequestMapping(method = RequestMethod.GET, value = ALL)
	public final HttpEntity<List<T>> listAll() {
		List<T> result = service.listAll();
		return ResponseEntity.ok(result);
	}

}
