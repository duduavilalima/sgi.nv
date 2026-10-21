package br.com.fasolti.commons.security.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService<U extends UserDetails> {

	U findUserByUsername(String username);

	String getUsername();

	U getUser();

}
