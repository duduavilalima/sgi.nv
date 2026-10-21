package br.com.fasolti.sgi.job.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converte expressões cron do Quartz (<code>1 = domingo</code>) para o formato do Spring (<code>0/7 = domingo</code>,
 * <code>1 = segunda</code>), deslocando os dias da semana numéricos.
 */
final class QuartzCron {

	private static final Pattern NUMBER = Pattern.compile("\\d+");

	private QuartzCron() {
	}

	static String toSpring(String quartz) {
		String[] fields = quartz.trim().split("\\s+");
		if (fields.length != 6 && fields.length != 7) {
			throw new IllegalArgumentException("Expressão cron inválida: " + quartz);
		}
		fields[5] = shiftDaysOfWeek(fields[5]);
		return String.join(" ", fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
	}

	private static String shiftDaysOfWeek(String field) {
		Matcher matcher = NUMBER.matcher(field);
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			int day = Integer.parseInt(matcher.group());
			matcher.appendReplacement(result, String.valueOf(day - 1));
		}
		matcher.appendTail(result);
		return result.toString();
	}
}
