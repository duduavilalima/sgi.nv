package br.com.fasolti.corporativo.configuration.service;

import br.com.fasolti.corporativo.configuration.domain.Parametro;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyService {

	private EntityManager entityManager;

	private Map<String, String> values;

	public PropertyService() {
	}

	public void initValues() {
		values = new HashMap<>();
		Query q = entityManager.createQuery("from Parametro");
		List<Parametro> resultList = q.getResultList();
		for (Parametro parametro : resultList) {
			values.put(parametro.getChave(), parametro.getValor());
		}
	}

	public String getString(String key) {
		return values.get(key);
	}

	public String getString(String key, String defaultVal) {
		if (!values.containsKey(key))
			return defaultVal;

		return values.get(key);
	}

	public Long getLong(String key) {
		return Long.valueOf(values.get(key));
	}

	public Long getLong(String key, Long defaultVal) {
		if (!values.containsKey(key))
			return defaultVal;

		return Long.valueOf(values.get(key));
	}

	public Integer getInt(String key) {
		return Integer.valueOf(values.get(key));
	}

	public Integer getInt(String key, Integer defaultVal) {
		if (!values.containsKey(key))
			return defaultVal;

		return Integer.valueOf(values.get(key));
	}

//	public static PropertyService getInstance() {
//		return (PropertyService)Component.getInstance("propertyService");
//	}
}
