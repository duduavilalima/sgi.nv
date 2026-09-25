package br.com.araujoabreu.sgi.util;

import br.com.fasolti.commons.lang.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class ResponseSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseSupport.class);

	protected ResponseEntity responseOk() {
		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
		return bodyBuilder.build();
	}

	protected ResponseEntity responseOk(Object obj) {
		return response(HttpStatus.OK, obj);
	}

	protected ResponseEntity responseServerError(Object obj) {
		if (!(obj instanceof ValidationException)) {
			LOGGER.error("Retornando erro", obj);
		}
		return response(HttpStatus.INTERNAL_SERVER_ERROR, obj);
	}

	/**
	 * Retornar ResponseEntity com status e objeto de retorno.
	 *
	 * @param status O status HttpStatus.
	 * @param obj    O objeto a ser transportado.
	 * @return ResponseEntity.
	 */
	protected ResponseEntity response(HttpStatus status, Object obj) {
		return response(status, null, obj);
	}

	/**
	 * Retornar um ResponseEntity com status, cabeçalho e objeto de retorno.
	 *
	 * @param status  O status HttpStatus.
	 * @param headers O mapa com o cabeçalho (chave, valor).
	 * @param obj     O objeto a ser transportado.
	 * @return O ResponseEntity.
	 */
	private ResponseEntity response(HttpStatus status, Map<String, String> headers, Object obj) {
		ResponseEntity responseBuilder = null;

		if (obj != null) {
			if (obj instanceof ValidationException) {
				responseBuilder = new ResponseEntity<>((ValidationException) obj, status);
			} else if (obj instanceof Throwable) {
				responseBuilder = new ResponseEntity<>((Throwable) obj, status);
			} else {
				responseBuilder = new ResponseEntity<>(obj, status);
			}
		} else {
			responseBuilder = new ResponseEntity<String>(status);
		}

		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				responseBuilder.getHeaders().add(entry.getKey(), entry.getValue());
			}
		}

		return responseBuilder;
	}
}
