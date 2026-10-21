package br.com.fasolti.commons.util;

import org.apache.commons.lang3.text.StrBuilder;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

public class EnumConverter implements CustomConverter {
	@Override
	public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
		if (source == null || destinationClass == null) {
			return null;
		}

		if (destinationClass.getSimpleName().equalsIgnoreCase("String")) {
			return source.toString();
		}

		if (destinationClass.isEnum()) {
			return getEnum(destinationClass, source);
		}

		throw new MappingException(new StrBuilder("Converter ")
			.append(this.getClass().getSimpleName())
			.append(" was used incorrectly. Arguments were: ")
			.append(destinationClass.getClass().getName())
			.append(" and ")
			.append(source).toString());
	}

	private Object getEnum(Class<?> destinationClass, Object source) {
		if (destinationClass.isEnum()) {
			for (Object valor : destinationClass.getEnumConstants()) {
				if (valor.toString().equals(source)) {
					return valor;
				}
			}
		}

		return null;
	}

}
