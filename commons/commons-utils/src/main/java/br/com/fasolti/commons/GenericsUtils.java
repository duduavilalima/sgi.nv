package br.com.fasolti.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {
	private GenericsUtils() {
	}

	public static GenericsInfo getGenericsInfo(Object instance) {
		Class<?>[] types = findGenericTypes(instance);
		return new GenericsInfo(types);
	}

	private static Class<?>[] findGenericTypes(Object instance) {
		Type type = instance.getClass().getGenericSuperclass();

		if (!(type instanceof ParameterizedType)) {
			type = instance.getClass().getSuperclass().getGenericSuperclass();
		}

		ParameterizedType genericType = ParameterizedType.class.cast(type);

		int numParams = genericType.getActualTypeArguments().length;
		Class<?>[] result = new Class[numParams];

		for (int i = 0; i < numParams; i++) {
			result[i] = (Class<?>) genericType.getActualTypeArguments()[i];
		}

		return result;
	}

}
