package br.com.fasolti.commons.util;

public interface TransactionalObject<S extends Enum<?>> {

	S getStatus();

	void setStatus(S status);

}
