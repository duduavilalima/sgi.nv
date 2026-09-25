package br.com.fasolti.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class FormatarBigDecimal {
	private static final NumberFormat fmtNumber;

	static {
		fmtNumber = NumberFormat.getInstance(new Locale("pt", "BR"));
		if (fmtNumber instanceof DecimalFormat) {
			DecimalFormat decimalFormat = (DecimalFormat) fmtNumber;
			decimalFormat.setParseBigDecimal(true);
		}
	}

	public static String formatBigDecimal(BigDecimal value) {
		if (value == null) return "";
		fmtNumber.setMinimumFractionDigits(value.scale());
		fmtNumber.setMaximumFractionDigits(value.scale());
		return fmtNumber.format(value);
	}

	public static BigDecimal parseBigDecimal(String value) {
		try {
			return (BigDecimal) fmtNumber.parse(value);
		} catch (ParseException e) {
			return new BigDecimal(0);
		}
	}
}
