package br.com.fasolti.commons.util;

public enum ServiceErrorType {
	INTERNAL(0),
	VALIDATION(1),
	DATABASE(2),
	// TODO Criar todos os tipos
	OTHER(999);

	private final int code;

	ServiceErrorType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ServiceErrorType findType(int code) {
		for (ServiceErrorType type : ServiceErrorType.values()) {
			if (type.getCode() == code)
				return type;
		}
		return OTHER;
	}
}
