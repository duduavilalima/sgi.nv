package br.com.fasolti.commons.util;

/**
 * Responsável por fazer o mapeamento entre classes que podem ser mapeadas (!?!?!)
 */
public interface ClassResolver {
	<R> Class<R> resolve(Class<?> clazz);
}
