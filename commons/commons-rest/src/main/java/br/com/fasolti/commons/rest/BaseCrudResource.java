package br.com.fasolti.commons.rest;

import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.client.service.SearchService;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public abstract class BaseCrudResource<T extends TransferObject, S extends SearchObject, SV extends CrudService<T>> extends BaseResource<T> {
	protected static final String DEFAULT_PAGE_SIZE = "20";

	protected static final String BATCH = "/batch";
	protected static final String SEARCH = "/search";
	protected static final String PAGE = "/page";

	protected static final String TRANSITION = ID + "/transition";

	@Autowired(required = false)
	private Logger logger;

	protected final SV service;

	protected final SearchService<T, S> searchService;

	public BaseCrudResource(SV service) {
		super(service);

		this.service = service;
		if (service instanceof SearchService) {
			this.searchService = (SearchService<T, S>) service;
		} else {
			this.searchService = null;
		}
	}

	protected final SV getService() {
		return service;
	}

	@RequestMapping(method = RequestMethod.GET, value = "")
	public final HttpEntity<T> create() {
		return ResponseEntity.ok(service.create());
	}

	@RequestMapping(method = RequestMethod.POST, value = "")
	public final HttpEntity<T> save(@RequestBody T object) {
		object = service.save(object);
		URI uri = URI.create(object.getId().toString());
		return ResponseEntity.created(uri).body(object);
	}

	@RequestMapping(method = RequestMethod.POST, value = BATCH)
	public final HttpEntity<List<T>> save(@RequestBody Collection<T> list) {
		//TODO Verificar qual é a melhor prática, já que não é possível retornar status CREATED para múltiplos objetos
		return ResponseEntity.ok(service.save(list));
	}

	@RequestMapping(method = RequestMethod.PUT, value = ID)
	public final HttpEntity<T> update(@RequestBody T object, @PathVariable Long id) {
		return ResponseEntity.ok(service.update(object, id));
	}

	@RequestMapping(method = RequestMethod.PUT, value = BATCH)
	public final HttpEntity<List<T>> update(@RequestBody Collection<T> list) {
		return ResponseEntity.ok(service.update(list));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = ID)
	public final HttpEntity<Void> delete(@PathVariable Long id) {
		T object = service.getById(id);
		service.delete(object);
		return ResponseEntity.noContent().build();
	}

//FIXME
//	@RequestMapping(method = RequestMethod.DELETE, value = BATCH)
//	public final HttpEntity<Void> delete(@RequestBody Collection<Long> idList) {
//		List<T> list = service.listAll(idList);
//		service.delete(list);
//		return ResponseEntity.noContent().build();
//	}

	protected Iterable<S> doAfterSearch(Iterable<S> list) {
		return list;
	}

	@RequestMapping(method = RequestMethod.POST, value = SEARCH)
	public final HttpEntity<Iterable<S>> searchByExample(@RequestBody S exampleObj) {
		Iterable<S> list = searchService.search(exampleObj);

		//FIXME logger.info("Retornando {} itens", list.size());

		return ResponseEntity.ok(doAfterSearch(list));
	}

	@RequestMapping(method = RequestMethod.POST, value = PAGE)
	public final ResponseEntity<Iterable<S>> searchByExample(
		@RequestBody S exampleObj,
		@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
		@RequestParam(defaultValue = "0") Integer pageNumber,
		@RequestParam(required = false) String sortField,
		@RequestParam(defaultValue = "asc") String sortDir) {

		Sort sort = null;
		if (sortField != null) {
			Sort.Direction dir = Sort.Direction.fromOptionalString(sortDir).orElse(null);
			if (dir != null) {
				sort = new Sort(dir, sortField);
			}
		}

		Pageable pageRequest = new PageRequest(pageNumber, pageSize, sort);

		List<S> list = Collections.emptyList(); //FIXME searchService.search(exampleObj, pageRequest);

		Page<S> resultPage = new PageImpl<>(list, pageRequest, list.size());

		logger.info("Retornando página {} de {} com {} itens de um total de {} itens",
			resultPage.getNumber(), resultPage.getTotalPages(),
			resultPage.getNumberOfElements(), resultPage.getTotalElements());

		return ResponseEntity.ok(doAfterSearch(resultPage));
	}

	protected Enum valueOfStatus(String status) {
		throw new NotImplementedException("valueOfStatus method must be implemented for TransitionalObject");
	}

	@RequestMapping(method = RequestMethod.PUT, value = TRANSITION)
	public final T transition(@PathVariable("id") Long id, @RequestParam("status") String statusName) throws Exception {
		Enum status = valueOfStatus(statusName);
		T object = service.getById(id);
		return service.transition(object, status);
	}

}
