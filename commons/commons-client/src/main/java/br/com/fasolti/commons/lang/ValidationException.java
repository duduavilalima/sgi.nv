package br.com.fasolti.commons.lang;

import br.com.fasolti.commons.util.ServiceErrorInfo;
import br.com.fasolti.commons.util.ServiceErrorType;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Usado nas validações de regras de negócios da aplicação.
 */
public class ValidationException extends ServiceException {

	private final List<ServiceErrorInfo> errorInfoList;

	public ValidationException(ServiceErrorInfo errorInfo) {
		super("Erro de validação: " + errorInfo.getCode());
		this.errorInfoList = ImmutableList.of(errorInfo);
	}

	public ValidationException(List<ServiceErrorInfo> errorInfoList) {
		super("Múltiplos erros de validação");
		this.errorInfoList = ImmutableList.copyOf(errorInfoList);
	}

	public ValidationException(String code, Object ... params) {
		this(new ServiceErrorInfo(ServiceErrorType.VALIDATION, code, params));
	}

	@Deprecated
	public ValidationException(String message) {
		this(new ServiceErrorInfo(ServiceErrorType.VALIDATION, ServiceErrorInfo.TYPE_UNKNOWN, message));
	}

	public List<ServiceErrorInfo> getErrorInfo() {
		return errorInfoList;
	}

	@Override
	public String getMessage() {
		StringBuilder b = new StringBuilder();

		// TODO Buscar e montar as mensagens na lingua default
		for (ServiceErrorInfo errorInfo : errorInfoList) {
			b.append(errorInfo.getCode()).append("\\n");
		}

		return b.toString();
	}
}
