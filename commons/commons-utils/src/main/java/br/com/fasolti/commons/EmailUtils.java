package br.com.fasolti.commons;

public final class EmailUtils {
	private EmailUtils() {
	}

	public static boolean validEmail(String email) {
		return Check.isNotEmpty(email) && email.length() <= 255 && email.matches(Constants.EMAIL_PATTERN);
	}
}
