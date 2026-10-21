package br.com.fasolti.commons.security.config;

import com.google.common.collect.Lists;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CsrfRequestMatcher implements RequestMatcher {
	private static final List<HttpMethod> allowedMethods = new ArrayList<HttpMethod>() {{
		add(HttpMethod.PATCH);
		add(HttpMethod.POST);
		add(HttpMethod.PUT);
		add(HttpMethod.DELETE);
	}};

	private List<String> paths;

	public CsrfRequestMatcher(String ... path) {
		paths = Lists.newArrayList(path);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		HttpMethod method = valueOf(request.getMethod());

		if (!allowedMethods.contains(method)) {
			return false;
		}

		String pathInfo = request.getPathInfo();

		for (String path : paths) {
			if (pathInfo.startsWith(path)) {
				return true;
			}
		}

		return false;
	}

	private static HttpMethod valueOf(String method) {
		try {
			return HttpMethod.valueOf(method);
		} catch (IllegalArgumentException e) {
		}

		return null;
	}

}
