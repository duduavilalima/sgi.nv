package br.com.fasolti.commons.security.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class CsrfTokenRepository implements org.springframework.security.web.csrf.CsrfTokenRepository {

	private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	private static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

	private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");

	private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

	private String headerName = DEFAULT_CSRF_HEADER_NAME;

	private String sessionAttributeName = DEFAULT_CSRF_TOKEN_ATTR_NAME;

	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		if (token == null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(sessionAttributeName);
			}
		} else {
			HttpSession session = request.getSession();
			session.setAttribute(sessionAttributeName, token);
		}
	}

	public CsrfToken loadToken(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		return (CsrfToken) session.getAttribute(sessionAttributeName);
	}

	public CsrfToken generateToken(HttpServletRequest request) {
		return new DefaultCsrfToken(headerName, parameterName, createNewToken());
	}

	public void setParameterName(String parameterName) {
		Assert.hasLength(parameterName, "parameterName cannot be null or empty");
		this.parameterName = parameterName;
	}

	public void setHeaderName(String headerName) {
		Assert.hasLength(headerName, "headerName cannot be null or empty");
		this.headerName = headerName;
	}

	public void setSessionAttributeName(String sessionAttributeName) {
		Assert.hasLength(sessionAttributeName, "sessionAttributename cannot be null or empty");
		this.sessionAttributeName = sessionAttributeName;
	}

	private String createNewToken() {
		return UUID.randomUUID().toString();
	}

}
