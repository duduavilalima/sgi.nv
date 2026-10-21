package br.com.fasolti.commons.rest;

import br.com.fasolti.commons.util.ServiceErrorInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceErrorFactory {
	private Map<String, ServiceErrorInfo> errorMap;

	public ServiceErrorFactory() {
		initErrorMap();
	}

	private void initErrorMap() {
		errorMap = new HashMap<>();

		//errorMap.put("validation.not-empty", new ServiceErrorInfo(ServiceErrorType.VALIDATION, "validation.not-empty"));
	}
}
