package br.com.fasolti.commons.email;

import javax.mail.internet.InternetAddress;
import java.util.Collection;

public final class Emails {

	private Emails() {
	}

	public static Email.Builder emailTo(String... addresses) {
		Email.Builder builder = new Email.Builder();
		for (String address : addresses) {
			builder.addTo(InternetAddresses.create(address));
		}
		return builder;
	}

	public static Email.Builder emailTo(Collection<InternetAddress> addresses) {
		Email.Builder builder = new Email.Builder();
		addresses.forEach(builder::addTo);
		return builder;
	}
}
