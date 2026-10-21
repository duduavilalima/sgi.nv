package br.com.fasolti.commons.lang;

public class InvalidTransitionExeception extends ServiceException {
	public InvalidTransitionExeception(Object object, Enum status, String message) {
		// TODO Planejar internacionalização
		super(String.format("Erro alterando o status de %s para %s: %s", object.toString(), status.toString(), message));
	}
}
