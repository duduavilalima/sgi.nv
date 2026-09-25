package br.com.fasolti.framework;

/**
 * Interface de constantes do módulo.
 *
 * @author <a href="mailto:fabiohsv@ciandt.com">fabiohsv</a>
 */
public interface Constants {
	String MESSAGES_BEGIN = "#{messages['";
	String MESSAGES_END = "']}";
	String EL_BEGIN = "#{";
	String EL_END = "}";
	String DATE_FORMAT_SQL_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	/**
	 * Constante horas de um dia
	 */
	int DAY = 24;

	interface SISTEMA {
		String USUARIO_SISTEMA = "sistema";
	}

	interface PATTERNS {
		String EMAIL_PATTERN = "[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*";
		String SIMPLE_DATE_PATTERN = "dd/MM/yyyy";
	}

}
