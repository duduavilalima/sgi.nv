package br.com.fasolti.commons.email;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

public final class InternetAddresses {

	private InternetAddresses() {
	}

	public static InternetAddress create(String address, String personal) {
		try {
			return new InternetAddress(address, personal, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static InternetAddress create(String address) {
		return create(address, null);
	}
}
