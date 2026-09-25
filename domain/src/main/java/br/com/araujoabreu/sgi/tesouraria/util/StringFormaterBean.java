package br.com.araujoabreu.sgi.tesouraria.util;

import br.com.araujoabreu.sgi.tesouraria.util.StringFormater.FieldType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringFormaterBean {

	private String sep = "";
	private final List<StringFormater> list = new ArrayList<>();

	public StringFormaterBean() {
	}

	public StringFormaterBean(String sep) {
		this.sep = sep;
	}

	public StringFormaterBean add(int size, FieldType type, String... values) {
		StringFormater sf = new StringFormater(size, type);
		for (String value : values) {
			sf.append(value);
		}
		this.list.add(sf);
		return this;
	}

	public StringFormaterBean add(int size, FieldType type, Date date) {
		StringFormater sf = new StringFormater(size, type);
		sf.append(date);
		this.list.add(sf);
		return this;
	}

	public StringFormaterBean add(int size, FieldType type, BigDecimal value, int digits) {
		if (value == null) {
			value = new BigDecimal(0);
		}

		StringFormater sf = new StringFormater(size + digits, type);
		sf.append(value, digits);
		this.list.add(sf);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (StringFormater sf : this.list) {
			buffer.append(sf.toString());
			buffer.append(this.sep);
		}
		String result = buffer.toString();
		if (!"".equals(sep) && result.endsWith(sep)) {
			return result.substring(0, result.length() - 1);
		}
		return result;

	}

}
