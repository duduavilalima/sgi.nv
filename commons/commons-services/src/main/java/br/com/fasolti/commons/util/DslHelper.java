package br.com.fasolti.commons.util;

import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.DomainObject;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DslHelper {
	@Autowired
	private BeanPathUtils beanPathUtils;

	private NumberPath<Long> getIdPath(EntityPath<? extends DomainObject> path) {
		return beanPathUtils.createLong(path, "id");
	}

	public Predicate eq(EntityPath<? extends DomainObject> path, TransferObject object) {
		if (object == null || object.getId() == null) {
			return null;
		}

		return getIdPath(path).eq(object.getId());
	}

	public Predicate eq(StringExpression expr, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		return expr.eq(value);
	}

	public Predicate eqIgnoreCase(StringExpression expr, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		return expr.lower().eq(value.toLowerCase());
	}

	public <T> Predicate eq(SimpleExpression<T> expr, T value) {
		if (value == null) {
			return null;
		}

		return expr.eq(value);
	}

	public Predicate isNull(SimpleExpression<?> expr, Boolean reference) {
		if (reference == null || !reference) {
			return null;
		}

		return expr.isNull();
	}

	public Predicate isNotNull(SimpleExpression<?> expr, Boolean reference) {
		if (reference == null || !reference) {
			return null;
		}

		return expr.isNotNull();
	}

	public Predicate between(DateExpression<Date> expr, Date start, Date end) {
		if (start == null && end == null) {
			return null;
		}

		if (start != null && end == null) {
			return expr.after(start);
		}

		if (start == null) {
			return expr.before(end);
		}

		return expr.between(start, end);
	}
}
