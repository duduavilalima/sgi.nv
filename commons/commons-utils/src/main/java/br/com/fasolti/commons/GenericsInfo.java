package br.com.fasolti.commons;

public class GenericsInfo {
	private final Class<?>[] types;

	GenericsInfo(Class<?>[] types) {
		this.types = types;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getType(int index) {
		return (Class<T>) types[index];
	}

	public <T> Class<T> getType() {
		return getType(0);
	}

	public Class<?>[] getTypes() {
		return types;
	}
}
