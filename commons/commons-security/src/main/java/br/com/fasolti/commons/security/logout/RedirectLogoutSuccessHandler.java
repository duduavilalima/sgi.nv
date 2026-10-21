package br.com.fasolti.commons.security.logout;

import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class RedirectLogoutSuccessHandler implements LogoutSuccessHandler {

	private String casLogoutUrl = "";
	private String serviceUrl = "";

	public RedirectLogoutSuccessHandler() {}

	public RedirectLogoutSuccessHandler(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}

	private String getRedirectUrl(ServletRequest request) {
		String redirectUrl = "";
		try {
			String realServiceUrl = URLEncoder.encode(serviceUrl, "UTF-8");
			redirectUrl = casLogoutUrl + "?service=" + realServiceUrl;

		} catch (UnsupportedEncodingException e) {
			// TODO Logging
			e.printStackTrace();
		}
		return redirectUrl;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		String redirectUrl = getRedirectUrl(request);
		if (!redirectUrl.isEmpty()) {
			response.sendRedirect(redirectUrl);
		}
	}

	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
