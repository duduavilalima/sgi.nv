package br.com.fasolti.commons;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public final class PasswordUtils {

	private static final int DEFAULT_PASSWORD_SIZE = 6;

	private static final String[] carct = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
		"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
		"u", "v", "w", "x", "y", "z",
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z"};

	@Autowired(required = false)
	private Logger logger;

	private PasswordUtils() {
	}

	public final String randomPassword() {
		return randomPassword(null);
	}

	/**
	 * Gera uma nova senha aleatória utilizando apenas letras e números.
	 *
	 * @param size O tamanho da senha. Se NULL, gera uma senha com tamanho 6.
	 * @return Uma nova senha com o tamanho informado.
	 */
	public final String randomPassword(Integer size) {
		if (size == null || size.intValue() <= 0) {
			size = DEFAULT_PASSWORD_SIZE;
		}
		String senha = Constants.BLANK;
		for (int x = 0; x < size; x++) {
			int j = (int) (Math.random() * carct.length);
			senha += carct[j];
		}
		return senha;
	}

	/**
	 * Transforma a senha em MD5.
	 *
	 * @param senha A senha a ser transformada.
	 * @return A senha em MD5.
	 */
	public final String hash(String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));

			return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error generating hash", e);
			return null;
		}
	}

}
