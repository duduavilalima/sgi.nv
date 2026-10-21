package br.com.fasolti.commons.services.security;

import br.com.fasolti.commons.GenericsUtils;
import br.com.fasolti.commons.security.user.SecurityService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseSecurityService<U extends UserDetails> implements SecurityService<U> {
	@Autowired(required = false)
	private Logger logger;

	private Class<U> userType;

	public BaseSecurityService() {
		userType = GenericsUtils.getGenericsInfo(this).getType();
	}

	protected Object getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new AuthenticationServiceException("Authentication not found in context");
		}

		Object principal = authentication.getPrincipal();

		if (principal == null) {
			throw new AuthenticationServiceException("No principal in context");
		}

		return principal;
	}

	@Override
	public String getUsername() {
		Object principal = getPrincipal();

		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}

		return principal.toString();
	}

	@Override
	public U getUser() {
		Object principal = getPrincipal();

		if ("anonymousUser".equals(principal.toString())) {
			throw new AccessDeniedException("Anonymous user not allowed");
		}

		if (!userType.isAssignableFrom(principal.getClass())) {
			throw new AccessDeniedException("Invalid Principal");
		}

		return userType.cast(principal);
	}
}
