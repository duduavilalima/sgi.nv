package br.com.fasolti.commons.util;

import br.com.fasolti.commons.client.service.Service;
import br.com.fasolti.commons.domain.DomainObject;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

//FIXME (vseixas) Não lembro porque comecei a fazer essa classe, mas parece uma boa ideia...
public class JPAServiceQuery<E extends DomainObject, S extends Service> extends JPAQuery<E> {
	private S service;

	@Override
	public List<E> fetch() {
		List<E> list = super.fetch();
		for (E object : list) {
//			service.doAfterLoad(object);
		}
		return list;
	}
}
