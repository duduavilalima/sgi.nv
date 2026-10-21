package br.com.fasolti.commons.rest;

import br.com.fasolti.commons.lang.InvalidTransitionExeception;
import br.com.fasolti.commons.lang.NotFoundException;
import br.com.fasolti.commons.lang.SearchNotAllowedException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.util.ServiceErrorInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExceptionHandling {
	@Autowired(required = false)
	private Logger logger;

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Void> notFound(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler(SearchNotAllowedException.class)
	public ResponseEntity<Void> searchNotAllowed(SearchNotAllowedException e) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<List<ServiceErrorInfo>> invalidObject(ValidationException e) {
		return ResponseEntity.badRequest().body(e.getErrorInfo());
	}

	@ExceptionHandler(InvalidTransitionExeception.class)
	public ResponseEntity<RestError> invalidTransition(InvalidTransitionExeception e) {
		logger.warn(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestError.from(e));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<RestError> authenticationException(AuthenticationException e) {
		logger.warn(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestError.from(e));
	}
}
