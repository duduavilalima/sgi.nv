package br.com.fasolti.security.crypto;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {
	private final static String DEFAULT_CHARSET = "UTF-8";

	private final static int ITERATIONS = 1000;
	private final static int SALT_LENGTH = 8;

	public boolean authenticate(String password, String digest) {
		String[] digestParts = digest.split("\\$");

		if (digestParts.length != 4)
			throw new IllegalArgumentException("Invalid password digest format");

		// Pega o algoritmo do digest
		HashAlgorithm algorithm;
		try {
			int algId = Integer.parseInt(digestParts[1]);
			algorithm = HashAlgorithm.get(algId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid password digest algorithm");
		}

		// Separa o salt e o hash da senha
		String salt = digestParts[2];
		String hash = digestParts[3];

		// Decodifica os dados em base64
		byte[] bHash = Base64.decodeBase64(hash);
		byte[] bSalt = Base64.decodeBase64(salt);

		// Codifica a senha proposta usando o mesmo algoritmo e o salt da senha real
		byte[] proposedHash = getHash(algorithm, ITERATIONS, password, bSalt);

		return Arrays.equals(proposedHash, bHash);
	}

	public String encrypt(HashAlgorithm algorithm, String password) {
		if (password == null)
			throw new IllegalArgumentException("Parameter 'password' is required");

		// Uses a secure Random not a simple Random
		SecureRandom random = new SecureRandom();

		// Salt generation 64 bits long
		byte[] bSalt = new byte[SALT_LENGTH];
		random.nextBytes(bSalt);

		// Digest computation
		byte[] bHash = getHash(algorithm, ITERATIONS, password, bSalt);

		// Converte o digest e o salt para base64
		String sHash = Base64.encodeBase64String(bHash);
		String sSalt = Base64.encodeBase64String(bSalt);

		return compose(algorithm.id, sHash, sSalt);
	}

	/**
	 * Compõe a string que deve ser armazenada de acordo com o padrão Unix SHA-crypt:
	 * $<ID>$<SALT>$<PWD>
	 */
	private String compose(int algId, String digest, String salt) {
		StringBuilder result = new StringBuilder();
		result.append("$").append(algId).append("$").append(salt).append("$").append(digest);
		return result.toString();
	}

	private byte[] getHash(HashAlgorithm algorithm, int iterations, String password, byte[] salt) throws DigestException {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm.code);
			digest.reset();
			digest.update(salt);
			byte[] input = digest.digest(password.getBytes(DEFAULT_CHARSET));
			for (int i = 0; i < iterations; i++) {
				digest.reset();
				input = digest.digest(input);
			}
			return input;
		} catch (Exception e) {
			throw new DigestException("Erro gerando hash de senha", e);
		}
	}

}
