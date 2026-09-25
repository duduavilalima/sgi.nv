package br.com.fasolti.sgi.service.util;

import br.com.araujoabreu.sgi.lang.SgiException;
import br.com.fasolti.sgi.lang.SgiParametroInexistenteException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

public interface ConfiguracaoService {

	Optional<String> getAny(String chave);

	default String get(String chave) {
		return getAny(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default <T> T get(String chave, Function<String, T> conversor) {
		return getAny(chave).map(conversor).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default <T> Optional<T> getAny(String key, Class<T> valueClass) {
		return getAny(key).map(v -> {
			try {
				return valueClass.getConstructor(String.class).newInstance(v);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new SgiException("Erro ao converter parâmetro '" + key + "' para classe '" + valueClass.getName() + "'.", e);
			}
		});
	}

	default <T> T get(String chave, Class<T> valueClass) {
		return getAny(chave, valueClass).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default Optional<Integer> getAnyAsInteger(String chave) {
		return getAny(chave).map(Integer::parseInt);
	}

	default Integer getAsInteger(String chave) {
		return getAnyAsInteger(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default Optional<Long> getAnyAsLong(String chave) {
		return getAny(chave).map(Long::parseLong);
	}

	default Long getAsLong(String chave) {
		return getAnyAsLong(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default Optional<Double> getAnyAsDouble(String chave) {
		return getAny(chave).map(Double::parseDouble);
	}

	default Double getAsDouble(String chave) {
		return getAnyAsDouble(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default Optional<BigInteger> getAnyAsBigInteger(String chave) {
		return getAny(chave).map(BigInteger::new);
	}

	default BigInteger getAsBigInteger(String chave) {
		return getAnyAsBigInteger(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}

	default Optional<BigDecimal> getAnyAsBigDecimal(String chave) {
		return getAny(chave).map(BigDecimal::new);
	}

	default BigDecimal getAsBigDecimal(String chave) {
		return getAnyAsBigDecimal(chave).orElseThrow(() -> new SgiParametroInexistenteException("Chave inexistente: " + chave));
	}
}
