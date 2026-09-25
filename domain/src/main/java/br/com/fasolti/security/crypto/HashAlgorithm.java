package br.com.fasolti.security.crypto;

public enum HashAlgorithm {
	SHA256(5, "SHA-256"),
	SHA512(6, "SHA-512");

	public final int id;
	public final String code;

	HashAlgorithm(int id, String code) {
		this.id = id;
		this.code = code;
	}

	public static HashAlgorithm get(int id) {
		for (HashAlgorithm a : values()) {
			if (a.id == id) return a;
		}
		return null;
	}
}
