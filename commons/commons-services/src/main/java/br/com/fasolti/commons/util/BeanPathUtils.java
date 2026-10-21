package br.com.fasolti.commons.util;

import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.domain.DomainObject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Component
public class BeanPathUtils {
	public <E extends DomainObject> NumberPath<Long> createLong(EntityPath<E> entityPath, String property) {
		PathBuilder<E> pathBuilder = new PathBuilder<>(entityPath.getType(), entityPath.getMetadata());
		return pathBuilder.getNumber(property, Long.class);
	}

	public <E extends DomainObject, S extends SearchObject> Predicate createSearchPredicate(EntityPath<E> entityPath, E entity) {
		BooleanBuilder builder = new BooleanBuilder();

		Map<String, Object> properties;
		try {
			properties = PropertyUtils.describe(entity);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			//FIXME Decidir o que fazer
			return builder.getValue();
		}

		PathBuilder<E> pathBuilder = new PathBuilder<>(entityPath.getType(), entityPath.getMetadata());

		for (String property : properties.keySet()) {
			Object value = properties.get(property);

			if (value == null) continue;

			if (value instanceof Long) {
				NumberPath<Long> path = pathBuilder.getNumber(property, Long.class);
				builder.and(path.eq((Long) value));
			} else if (value instanceof String) {
				StringPath path = pathBuilder.getString(property);
				builder.and(path.containsIgnoreCase((String) value));
			} else { //TODO Incluir outros tipos
				builder.and(pathBuilder.get(property).eq(value));
			}
		}

		return builder.getValue();
	}
}
