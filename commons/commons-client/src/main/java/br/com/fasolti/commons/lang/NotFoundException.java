package br.com.fasolti.commons.lang;

public class NotFoundException extends ServiceException {

	public NotFoundException(Long id) {
		super(String.format("Objeto com ID %d não encontrado", id));
	}

	public NotFoundException(Class<?> entityType, Long id) {
		super(String.format("%s com ID %d não encontrado", entityType.getSimpleName(), id));
	}

}
