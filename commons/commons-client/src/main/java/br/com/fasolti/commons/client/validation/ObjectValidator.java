package br.com.fasolti.commons.client.validation;

import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.util.ServiceErrorInfo;
import br.com.fasolti.commons.util.ServiceErrorType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe usada na validação automática dos TOs.
 */
public class ObjectValidator<T extends TransferObject> {

	@Autowired(required = false)
	private Logger logger;

	public void validate(T object) throws ValidationException {
		List<ServiceErrorInfo> errors = new ArrayList<>();

		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Validation.class)) {
				Validation validation = field.getAnnotation(Validation.class);

				try {
					field.setAccessible(Boolean.TRUE);

					if (validation.notNull()) {
						if (field.get(object) == null) {
							errors.add(createError("validation.not-null", validation.name()));
							continue;
						}
					} else {
						if (field.get(object) == null) {
							continue;
						}
					}

					if (field.getType() == String.class) {
						String valueString = (String) field.get(object);
						if (valueString.length() < validation.min()) {
							errors.add(createError("validation.min-size", validation.name(), validation.min()));
						}
						if (validation.max() > 0 && valueString.length() > validation.max()) {
							errors.add(createError("validation.max-size", validation.name(), validation.max()));
						}
						continue;
					}

					if (field.get(object) instanceof Collection) {
						Collection<?> valueCollection = (Collection<?>) field.get(object);
						if (valueCollection.isEmpty()) {
							errors.add(createError("validation.not-empty", validation.name()));
						}
						continue;
					}
				} catch (IllegalAccessException e) {
					errors.add(new ServiceErrorInfo(ServiceErrorType.INTERNAL, "validation.invalid-class", field.getName(), e.getMessage()));
				}
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	private ServiceErrorInfo createError(String code, String message, Object ... params) {
		return new ServiceErrorInfo(ServiceErrorType.VALIDATION, code, message, params);
	}
}
