package br.com.fasolti.commons.services.security;

import br.com.fasolti.commons.security.user.SecurityService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private SecurityService<?> securityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = securityService.findUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("Usuário %s não cadastrado", username));
		}

		logger.debug("Usuário com login '{}' encontrado: {}", username, user);

		return user;
	}
}
