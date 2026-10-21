package br.com.fasolti.commons.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyPasswordEncoder extends BCryptPasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return super.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return true;
	}
}
