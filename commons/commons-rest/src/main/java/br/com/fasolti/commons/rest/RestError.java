package br.com.fasolti.commons.rest;

public class RestError {
	private String message;

	public static RestError from(Exception e) {
		return new RestError(e.getMessage());
	}

	public static RestError from(String error) {
		return new RestError(error);
	}

	private RestError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
