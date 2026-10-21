package br.com.fasolti.commons.util;

import br.com.fasolti.commons.domain.DomainObject;
import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;

@Component
public class Dereferencer {
	@PersistenceContext
	private EntityManager entityManager;

	private static final ThreadLocal<Kryo> kryo = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			return new Kryo();
		}
	};

	public void process(DomainObject entity) {
		IdentityHashMap<Object, Object> map = new IdentityHashMap<>();
		firstLevel(entity, map);
	}

	private void firstLevel(Object entity, IdentityHashMap map) {
		// Tira a entidade do EntityManager para poder
		// trocar IDs de entidades dependentes sem confundir o Hibernate
		entityManager.detach(entity);

		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(entity);

		for (PropertyDescriptor prop : descriptors) {
			if (DomainObject.class.isAssignableFrom(prop.getPropertyType())) {
				Object propValue = getPropertyValue(entity, prop);

				if (propValue != null) {
					if (map.containsKey(propValue)) {
						propValue = kryo.get().copy(propValue);
						setPropertyValue(entity, prop, propValue);
					}

					map.put(propValue, null);

					secondLevel(propValue, map);
				}
			}
		}
	}

	private void secondLevel(Object entity, IdentityHashMap map) {
		entityManager.detach(entity);

		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(entity);

		for (PropertyDescriptor prop : descriptors) {
			if (DomainObject.class.isAssignableFrom(prop.getPropertyType())) {
				setPropertyValue(entity, prop, null);
			}
		}
	}

	private Object getPropertyValue(Object bean, PropertyDescriptor prop) {
		try {
			return prop.getReadMethod().invoke(bean);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Criar uma RuntimeException
			return null;
		}
	}

	private void setPropertyValue(Object bean, PropertyDescriptor prop, Object value) {
		try {
			prop.getWriteMethod().invoke(bean, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Criar uma RuntimeException
		}
	}
}
