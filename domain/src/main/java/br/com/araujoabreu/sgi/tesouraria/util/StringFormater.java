package br.com.araujoabreu.sgi.tesouraria.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormater {

	public final StringBuffer buffer = new StringBuffer();
	public final int size;
	public final FieldType type;
	private Date date;

	public StringFormater(int size, FieldType type) {
		this.type = type;
		this.size = size;
	}

	public StringFormater append(String str) {
		buffer.append(str == null ? "" : str.trim());
		return this;
	}

	public StringFormater append(Date date) {
		this.date = date;
		return this;
	}

	public StringFormater append(BigDecimal bd, int digits) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(digits);
		nf.setMaximumFractionDigits(digits);
		nf.setGroupingUsed(false);
		String value = nf.format(bd);
		buffer.append(value.replaceAll("\\.", "").replaceAll(",", ""));
		return this;
	}

	private StringBuffer getPattern() {
		char c = ' ';

		if (this.type == FieldType.Numeric || this.type == FieldType.Double) {
			c = '0';
		}

		StringBuffer buffer = new StringBuffer(size);
		for (int i = 0; i < this.size; i++) {
			buffer.append(c);
		}

		return buffer;
	}

	private String onlyNumbers(String str) {
		return str.replaceAll("\\D+", "");
	}

	@Override
	public String toString() {
		StringBuffer pattern = getPattern();
		if (this.type == FieldType.Numeric) {
			pattern.append(onlyNumbers(this.buffer.toString()));
			int realSize = pattern.length();
			return pattern.toString().substring(realSize - this.size, realSize);
		} else if (this.type == FieldType.Double) {
			pattern.append(this.buffer);
			int realSize = pattern.length();
			return pattern.toString().substring(realSize - this.size, realSize);
		} else if (this.type == FieldType.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy"); //FIXME
			return sdf.format(date);
		} else if (this.type == FieldType.Time) {
			SimpleDateFormat sdf = new SimpleDateFormat("hhmmss"); //FIXME
			return sdf.format(date);
		}
		return this.buffer.append(pattern).toString().substring(0, size);
	}

	public enum FieldType {
		Alpha, Numeric, Date, Time, Double
	}

}
