package br.com.fasolti.commons.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultObjectEntityClassResolver implements ClassResolver {
	private static final Map<Class<?>, Class<?>> CLASS_MAP = new HashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <R> Class<R> resolve(Class<?> clazz) {
		Class<R> resultClass = (Class<R>) CLASS_MAP.get(clazz);

		// Convert class name
		if (resultClass == null) {
			String originalClassName = clazz.getCanonicalName();

			String resultClassName;
			if (originalClassName.endsWith("TO")) {
				String pattern = "(.*)\\.transfer\\.(.*)TO";
				resultClassName = originalClassName.replaceAll(pattern, "$1.domain.$2");
			} else {
				String pattern = "(.*)\\.domain\\.(.*)";
				resultClassName = originalClassName.replaceAll(pattern, "$1.transfer.$2TO");
			}

			try {
				resultClass = (Class<R>) Class.forName(resultClassName);
			} catch (ClassNotFoundException e) {
				// TODO Melhorar exceção
				throw new RuntimeException("Erro mapeando", e);
			}

			CLASS_MAP.put(clazz, resultClass);
		}

		return resultClass;
	}
}
