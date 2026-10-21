package br.com.fasolti.commons.util;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class ServiceErrorInfo implements Serializable {

	public static final String TYPE_UNKNOWN = "unknown";

	private final ServiceErrorType type;

	private final String code;

	private final List<Object> params;

	public ServiceErrorInfo(String code) {
		this(ServiceErrorType.OTHER, code);
	}

	public ServiceErrorInfo(int type, String code) {
		this(ServiceErrorType.findType(type), code);
	}

	public ServiceErrorInfo(int type, String code, Object ... params) {
		this(ServiceErrorType.findType(type), code, params);
	}

	public ServiceErrorInfo(ServiceErrorType type, String code, Object ... params) {
		this.type = type;
		this.code = code;
		this.params = Lists.newArrayList(params);
	}

	public String getTypeDesc() {
		return type.name();
	}

	public int getType() {
		return type.getCode();
	}

	public String getCode() {
		return code;
	}

	public List<Object> getParams() {
		return params;
	}
}
