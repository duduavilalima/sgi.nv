package br.com.araujoabreu.sgi.tesouraria.util.boletoUtil;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;

public abstract class ObjectUtil {

	public static boolean isNotNull(Object obj) {
		if (obj == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Object obj) {
		return !isNotNull(obj);
	}

	public static boolean isNotNull(String str) {
		return checkStringNotEmpty(str);
	}

	public static boolean isNull(String str) {
		return !checkStringNotEmpty(str);
	}

	public static boolean checkStringNotEmpty(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean checkStringEmpty(String str) {
		return !checkStringNotEmpty(str);
	}

	public static boolean isNotEmpty(Collection<?> obj) {
		if (obj == null || obj.isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(Collection<?> obj) {
		return !isNotEmpty(obj);
	}

	public static void isNotNull(Object obj, String msgValidacao) {
		if (obj == null) {
			throw new NullPointerException(msgValidacao);
		}
	}

	public static void checkNull(Object obj, String msgValidacao) throws Exception {
		if (obj != null) {
			throw new Exception(msgValidacao);
		}
	}

	public static String toString(Object obj) {
		return ToStringBuilder.reflectionToString(obj);
	}

}
