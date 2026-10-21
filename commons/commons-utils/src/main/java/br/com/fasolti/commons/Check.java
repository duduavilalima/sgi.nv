package br.com.fasolti.commons;

import java.util.Collection;

public final class Check {

	private Check() {
	}

	public static boolean isAssigned(Object obj) {
		return obj != null && !Constants.BLANK.equals(obj.toString());
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isEmpty(String str) {
		return str == null || Constants.BLANK.equals(str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return str != null && !Constants.BLANK.equals(str.trim());
	}

	public static boolean isEmpty(StringBuffer buffer) {
		return isEmpty(buffer.toString());
	}

	/**
	 * Verdadeiro se o objeto não for nulo e, quando texto ou coleção, não estiver vazio.
	 */
	public static boolean isNotEmpty(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() > 0;
		}
		if (obj instanceof Collection) {
			return !((Collection<?>) obj).isEmpty();
		}
		return true;
	}

	public static boolean isEmpty(Object obj) {
		return !isNotEmpty(obj);
	}

	public static boolean isNotEmpty(StringBuffer buffer) {
		return isNotEmpty(buffer.toString());
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
