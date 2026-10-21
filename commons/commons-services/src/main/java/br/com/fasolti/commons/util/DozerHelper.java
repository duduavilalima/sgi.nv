package br.com.fasolti.commons.util;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DozerHelper {

	private static final Map<Class<?>, Class<?>> classMap = new HashMap<>();

	private final Mapper mapper;

	@Autowired
	public DozerHelper(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Apenas um proxy para o Mapper
	 */
	public final <U, V> U map(V source, Class<U> destinationClass) {
		return mapper.map(source, destinationClass);
	}

	/**
	 * Faz o mapeamento entre listas de tipos diferentes
	 */
	public final <T, U> List<U> map(Iterable<T> source, Class<U> destType) {
		List<U> dest = new ArrayList<>();

		for (T element : source) {
			if (element == null) {
				continue;
			}
			dest.add(mapper.map(element, destType));
		}

		// finally remove all null values if any
		List<U> s1 = new ArrayList<>();
		s1.add(null);
		dest.removeAll(s1);

		return dest;
	}

	public final <U> U map(Object mapeable, ClassResolver classResolver) {
		Class<U> resultClass = classResolver.resolve(mapeable.getClass());
		return mapper.map(mapeable, resultClass);
	}

	public final <U> List<U> map(Iterable<?> mapeableList, ClassResolver classResolver) {
		if (mapeableList == null || !mapeableList.iterator().hasNext())
			return Collections.emptyList();

		Class<U> resultClass = classResolver.resolve(mapeableList.iterator().next().getClass());
		return this.map(mapeableList, resultClass);
	}
}
