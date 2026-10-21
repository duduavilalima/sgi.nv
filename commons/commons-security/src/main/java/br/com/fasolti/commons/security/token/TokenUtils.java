package br.com.fasolti.commons.security.token;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class TokenUtils {

	private static final byte[] MAGIC_KEY = "pyMeGMmfDE235Y4Z".getBytes();

	private static final long MILLIS_TO_MIN = 1000L * 60;

	private static final int DEFAULT_TIMEOUT = 60; // 1 hour

	private static final int API_TIMEOUT = 360; // 6 hours

	private static final String TOKEN_FORMAT = "%s:%s:%s:%s";

	private static final String DIGEST_ALGORITHM = "SHA-256";

	private enum Scope {
		ALL, API;
	}

	public String createToken(UserDetails userDetails) {
		long expires = System.currentTimeMillis() + MILLIS_TO_MIN * DEFAULT_TIMEOUT;
		return buildToken(userDetails, expires, Scope.ALL);
	}

	public String createAPIToken(UserDetails userDetails) {
		long expires = System.currentTimeMillis() + MILLIS_TO_MIN * API_TIMEOUT;
		return buildToken(userDetails, expires, Scope.API);
	}

	private String computeSignature(UserDetails userDetails, long expires, Scope scope) {
		MessageDigest digest = createDigest();

		digest.update(userDetails.getUsername().getBytes());
		digest.update(Long.toString(expires).getBytes());
		digest.update(userDetails.getPassword().getBytes());
		digest.update(MAGIC_KEY);

		return new String(Hex.encode(digest.digest()));
	}

	private String buildToken(UserDetails userDetails, long expires, Scope scope) {
		String signature = computeSignature(userDetails, expires, scope);
		return String.format(TOKEN_FORMAT, userDetails.getUsername(), expires, scope.name(), signature);
	}

	public String getUserName(String authToken) {
		if (null == authToken) {
			return null;
		}

		String[] parts = authToken.split(":");
		return parts[0];
	}

	public String getScope(String authToken) {
		if (null == authToken) {
			return "NONE";
		}

		String[] parts = authToken.split(":");
		return parts[2];
	}

	public boolean validateToken(String authToken, UserDetails userDetails) {
		String[] parts = authToken.split(":");

		long expires = Long.parseLong(parts[1]);
		Scope scope = Scope.valueOf(parts[2]);
		String signature = parts[3];

		if (expires < System.currentTimeMillis()) {
			return false;
		}

		return signature.equals(computeSignature(userDetails, expires, scope));
	}

	private MessageDigest createDigest() {
		try {
			return MessageDigest.getInstance(DIGEST_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(String.format("No %s algorithm available", DIGEST_ALGORITHM));
		}
	}

}
