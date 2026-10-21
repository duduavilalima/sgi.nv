package br.com.fasolti.commons.client.auth;

public class TokenTransfer {

	private final String token;

	public TokenTransfer(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

}
