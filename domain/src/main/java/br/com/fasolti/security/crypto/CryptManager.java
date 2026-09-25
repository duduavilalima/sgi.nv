package br.com.fasolti.security.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe usada para criptografar e decriptografar textos.
 *
 * @author Rodrigo Barreiros
 */

public class CryptManager {

	/**
	 * Método de obtenção da hash MD5 de uma senha utilizada na comparação da senha do usuário.
	 *
	 * @param senha String da senha sem criptografia.
	 * @return Hash da senha
	 * @throws EncryptionException
	 * @author Sidharta Noleto
	 */
	public String hashMD5(String senha) throws EncryptionException {
		StringBuilder hexString = new StringBuilder();

		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			byte[] messageDigest = algorithm.digest(senha.getBytes());

			for (byte b : messageDigest) {
				String hex = Integer.toHexString(0xFF & b);

				if (hex.length() == 1) {
					hexString.append('0');
				}

				hexString.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e.getMessage(), e);
		}

		return hexString.toString();
	}

	public static class EncryptionException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EncryptionException(String message, Throwable t) {
			super(message, t);
		}

		public EncryptionException(String message) {
			super(message);
		}
	}

}
