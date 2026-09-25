package br.com.araujoabreu.sgi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = -8950728034294884664L;

	public CustomObjectMapper() {
		super();
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
}
